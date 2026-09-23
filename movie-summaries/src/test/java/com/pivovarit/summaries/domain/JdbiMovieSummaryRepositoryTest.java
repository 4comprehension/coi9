package com.pivovarit.summaries.domain;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import javax.sql.DataSource;

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

        assertThat(created).isEqualTo(1L);
        assertThat(updated).isEqualTo(2L);
        assertThat(repository.getSummary(movieId)).contains("second summary");
    }

    @Test
    void shouldBumpVersionOnEveryUpsert() {
        var movieId = 2L;

        repository.upsert(movieId, "first summary");
        repository.upsert(movieId, "second summary");
        var version = repository.upsert(movieId, "third summary");

        assertThat(version).isEqualTo(3L);
    }
}
