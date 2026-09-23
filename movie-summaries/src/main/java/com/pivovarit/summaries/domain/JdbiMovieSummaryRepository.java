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
    public long upsert(long movieId, String summary) {
        return jdbi.withHandle(handle -> upsert(handle, movieId, summary));
    }

    @Override
    public long upsert(TransactionContext context, long movieId, String summary) {
        return upsert(((JdbiTransactionContext) context).handle(), movieId, summary);
    }

    private long upsert(Handle handle, long movieId, String summary) {
        return handle
          .createQuery("""
            INSERT INTO movie_summaries (movie_id, summary, version)
            VALUES (:movieId, :summary, 1)
            ON CONFLICT (movie_id) DO UPDATE SET summary = excluded.summary, version = movie_summaries.version + 1
            RETURNING version
            """)
          .bind("movieId", movieId)
          .bind("summary", summary)
          .mapTo(Long.class)
          .one();
    }
}
