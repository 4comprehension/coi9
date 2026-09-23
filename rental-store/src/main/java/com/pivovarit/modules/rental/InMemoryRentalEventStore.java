package com.pivovarit.modules.rental;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryRentalEventStore implements RentalEventStore {

    private final Map<String, List<RentalEvent>> streams = new ConcurrentHashMap<>();

    @Override
    public void append(RentalEvent event, long expectedVersion) {
        streams.compute(event.userEmail(), (userEmail, current) -> {
            List<RentalEvent> stream = current == null ? List.of() : current;
            if (stream.size() != expectedVersion) {
                throw new ConcurrentRentalModificationException(userEmail, expectedVersion);
            }
            List<RentalEvent> next = new ArrayList<>(stream);
            next.add(event);
            return List.copyOf(next);
        });
    }

    @Override
    public List<RentalEvent> findByUser(String userEmail) {
        return streams.getOrDefault(userEmail, List.of());
    }
}
