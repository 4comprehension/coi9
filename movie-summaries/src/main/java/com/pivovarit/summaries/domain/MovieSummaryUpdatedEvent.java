package com.pivovarit.summaries.domain;

import java.time.Instant;

public record MovieSummaryUpdatedEvent(long movieId, String summary, Instant updatedAt) {
}
