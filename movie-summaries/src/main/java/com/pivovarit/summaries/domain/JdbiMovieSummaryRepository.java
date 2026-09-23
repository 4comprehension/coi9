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
    public boolean upsert(long movieId, String summary) {
        return jdbi.withHandle(handle -> upsert(handle, movieId, summary));
    }

    @Override
    public boolean upsert(Handle handle, long movieId, String summary) {
        return handle
          .createQuery("""
            INSERT INTO movie_summaries (movie_id, summary)
            VALUES (:movieId, :summary)
            ON CONFLICT (movie_id) DO UPDATE SET summary = excluded.summary
            RETURNING (xmax = 0) AS inserted
            """)
          .bind("movieId", movieId)
          .bind("summary", summary)
          .mapTo(Boolean.class)
          .one();
    }
}
