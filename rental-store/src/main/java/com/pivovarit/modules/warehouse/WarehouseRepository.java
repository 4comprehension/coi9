package com.pivovarit.modules.warehouse;

import com.pivovarit.modules.rental.MovieId;

interface WarehouseRepository {
    int countCopies(MovieId id);
    void addCopies(MovieId id, int copies);
    boolean takeCopy(MovieId id);
}
