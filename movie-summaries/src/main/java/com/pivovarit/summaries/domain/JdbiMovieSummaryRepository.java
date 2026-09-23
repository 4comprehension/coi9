package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.sql.DataSource;
import java.util.Optional;

class JdbiMovieSummaryRepository implements MovieSummaryRepository {

    private final Jdbi jdbi;

    JdbiMovieSummaryRepository(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public Optional<String> getSummary(long movieId) {
        return jdbi.withHandle(handle -> handle
          .createQuery("SELECT summary FROM movie_summaries WHERE movie_id = :movieId")
          .bind("movieId", movieId)
          .mapTo(String.class)
          .findFirst());
    }

    @Override
    public Optional<Long> upsert(long movieId, String summary) {
        return jdbi.withHandle(handle -> upsert(handle, movieId, summary));
    }

    @Override
    public Optional<Long> upsert(TransactionContext context, long movieId, String summary) {
        return upsert(JdbiTransactionContext.handleOf(context), movieId, summary);
    }

    private Optional<Long> upsert(Handle handle, long movieId, String summary) {
        Optional<Long> currentVersion = currentVersion(handle, movieId);

        if (currentVersion.isEmpty()) {
            int inserted = handle
              .createUpdate("""
                INSERT INTO movie_summaries (movie_id, summary, version)
                VALUES (:movieId, :summary, 1)
                ON CONFLICT (movie_id) DO NOTHING
                """)
              .bind("movieId", movieId)
              .bind("summary", summary)
              .execute();

            return inserted == 1 ? Optional.of(1L) : Optional.empty();
        }

        long expectedVersion = currentVersion.get();
        long nextVersion = expectedVersion + 1;

        int updated = handle
          .createUpdate("""
            UPDATE movie_summaries
            SET summary = :summary, version = :nextVersion
            WHERE movie_id = :movieId AND version = :expectedVersion
            """)
          .bind("movieId", movieId)
          .bind("summary", summary)
          .bind("nextVersion", nextVersion)
          .bind("expectedVersion", expectedVersion)
          .execute();

        return updated == 1 ? Optional.of(nextVersion) : Optional.empty();
    }

    private Optional<Long> currentVersion(Handle handle, long movieId) {
        return handle
          .createQuery("SELECT version FROM movie_summaries WHERE movie_id = :movieId")
          .bind("movieId", movieId)
          .mapTo(Long.class)
          .findFirst();
    }
}
