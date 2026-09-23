package com.pivovarit.summaries.domain;

import java.util.Optional;

public interface MovieSummaryRepository {
    Optional<String> getSummary(long movieId);

    /**
     * Attempts a single optimistic-locking write: create the row, or update it against the
     * version last seen. Does not retry on conflict.
     *
     * @return the resulting version, or empty if the row was concurrently modified
     */
    Optional<Long> upsert(long movieId, String summary);
    Optional<Long> upsert(TransactionContext context, long movieId, String summary);
}
