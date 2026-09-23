package com.pivovarit.summaries.domain;

import java.util.Optional;

public interface MovieSummaryRepository {
    Optional<String> getSummary(long movieId);
    long upsert(long movieId, String summary);
    long upsert(TransactionContext context, long movieId, String summary);
}
