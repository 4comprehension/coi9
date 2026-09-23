package com.pivovarit.modules.rental;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryRentalEventStoreTest {

    private static final String ALICE = "alice@example.com";

    @Test
    void shouldRejectAppendWithStaleVersion() {
        var store = new InMemoryRentalEventStore();
        store.append(rented(1), 0);

        assertThatThrownBy(() -> store.append(rented(2), 0))
          .isInstanceOf(ConcurrentRentalModificationException.class);
        assertThat(store.findByUser(ALICE)).hasSize(1);
    }

    @Test
    void shouldKeepIndependentVersionsPerUser() {
        var store = new InMemoryRentalEventStore();
        store.append(rented(1), 0);

        store.append(new RentalEvent.MovieRented(new MovieId(1), "bob@example.com", Instant.now()), 0);

        assertThat(store.findByUser("bob@example.com")).hasSize(1);
    }

    private static RentalEvent rented(long movieId) {
        return new RentalEvent.MovieRented(new MovieId(movieId), ALICE, Instant.now());
    }
}
