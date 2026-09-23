package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;

import java.util.Optional;

public interface MovieSummaryRepository {
    Optional<String> getSummary(long movieId);
    boolean upsert(long movieId, String summary);
    boolean upsert(Handle handle, long movieId, String summary);
}
