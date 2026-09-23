package com.pivovarit.summaries.domain;

import java.time.Instant;

public record MovieSummaryUpdatedEvent(long movieId, String summary, long version, Instant updatedAt) implements OutboxEvent {

    public static final String ROUTING_KEY = "movie-summary.updated";

    @Override
    public String getType() {
        return ROUTING_KEY;
    }
}
