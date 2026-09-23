package com.pivovarit.modules.rental;

import java.time.Instant;

sealed interface RentalEvent {

    MovieId movieId();
    String userEmail();
    Instant occurredAt();

    record MovieRented(MovieId movieId, String userEmail, Instant occurredAt) implements RentalEvent {
    }

    record MovieReturned(MovieId movieId, String userEmail, Instant occurredAt) implements RentalEvent {
    }
}
