package com.pivovarit.modules.warehouse;

import com.pivovarit.modules.rental.MovieId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryWarehouseRepository implements WarehouseRepository {

    private final Map<MovieId, Integer> copies = new ConcurrentHashMap<>();

    @Override
    public int countCopies(MovieId id) {
        return copies.getOrDefault(id, 0);
    }

    @Override
    public void addCopies(MovieId id, int count) {
        copies.merge(id, count, Integer::sum);
    }

    @Override
    public boolean takeCopy(MovieId id) {
        boolean[] taken = {false};
        copies.computeIfPresent(id, (key, count) -> {
            if (count > 0) {
                taken[0] = true;
                return count - 1;
            }
            return count;
        });
        return taken[0];
    }
}
