package com.pivovarit.modules.rental.api;

import java.time.Instant;

public record MovieSummaryUpdatedEvent(long movieId, String summary, Instant updatedAt) {
}
