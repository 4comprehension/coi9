package com.pivovarit.summaries.domain;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class JdbiMovieSummaryRepositoryTest {

    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18")
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("password");

    static DataSource dataSource;

    @BeforeAll
    static void migrate() {
        Flyway.configure()
          .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
          .load()
          .migrate();

        var pgDataSource = new PGSimpleDataSource();
        pgDataSource.setUrl(postgres.getJdbcUrl());
        pgDataSource.setUser(postgres.getUsername());
        pgDataSource.setPassword(postgres.getPassword());
        dataSource = pgDataSource;
    }

    private final MovieSummaryRepository repository = new JdbiMovieSummaryRepository(dataSource);

    @Test
    void shouldReturnEmptyWhenSummaryDoesNotExist() {
        assertThat(repository.getSummary(999L)).isEmpty();
    }

    @Test
    void shouldCreateThenUpdateSummary() {
        var movieId = 1L;

        var created = repository.upsert(movieId, "first summary");
        var updated = repository.upsert(movieId, "second summary");

        assertThat(created).contains(1L);
        assertThat(updated).contains(2L);
        assertThat(repository.getSummary(movieId)).contains("second summary");
    }

    @Test
    void shouldBumpVersionOnEveryUpsert() {
        var movieId = 2L;

        repository.upsert(movieId, "first summary");
        repository.upsert(movieId, "second summary");
        var version = repository.upsert(movieId, "third summary");

        assertThat(version).contains(3L);
    }

    @Test
    void shouldReportConflictInsteadOfRetryingOnConcurrentUpdate() throws Exception {
        var movieId = 3L;
        int concurrentWriters = 32;

        repository.upsert(movieId, "initial summary");

        ExecutorService executor = Executors.newFixedThreadPool(concurrentWriters);
        try {
            var barrier = new CyclicBarrier(concurrentWriters);
            List<Callable<Optional<Long>>> writes = IntStream.range(0, concurrentWriters)
              .<Callable<Optional<Long>>>mapToObj(i -> () -> {
                  barrier.await();
                  return repository.upsert(movieId, "summary " + i);
              })
              .toList();

            var results = executor.invokeAll(writes).stream()
              .map(future -> {
                  try {
                      return future.get();
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }
              })
              .toList();

            var successfulVersions = results.stream().filter(Optional::isPresent).map(Optional::get).toList();

            assertThat(results).anyMatch(Optional::isEmpty);
            assertThat(successfulVersions).doesNotHaveDuplicates();
        } finally {
            executor.shutdown();
        }
    }
}
