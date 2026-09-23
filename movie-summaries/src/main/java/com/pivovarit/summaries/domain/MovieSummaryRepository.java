package com.pivovarit.summaries.domain;

import java.util.Optional;

public interface MovieSummaryRepository {
    Optional<String> getSummary(long movieId);
    boolean upsert(long movieId, String summary);
    boolean upsert(TransactionContext context, long movieId, String summary);
}
