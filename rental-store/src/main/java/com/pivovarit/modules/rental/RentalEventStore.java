package com.pivovarit.modules.rental;

import java.util.List;

interface RentalEventStore {

    /**
     * Appends an event to the user's stream as version {@code expectedVersion + 1}.
     *
     * @throws ConcurrentRentalModificationException if the stream has moved past {@code expectedVersion}
     */
    void append(RentalEvent event, long expectedVersion);

    /**
     * Returns the user's events in order; the stream's current version equals the list's size.
     */
    List<RentalEvent> findByUser(String userEmail);
}
