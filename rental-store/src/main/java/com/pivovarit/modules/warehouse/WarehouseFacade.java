package com.pivovarit.modules.warehouse;

import com.pivovarit.modules.rental.MovieId;

public class WarehouseFacade {

    private final WarehouseRepository warehouseRepository;

    WarehouseFacade(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public int availableCopies(MovieId id) {
        return warehouseRepository.countCopies(id);
    }

    public void restock(MovieId id, int copies) {
        warehouseRepository.addCopies(id, copies);
    }

    public boolean reserve(MovieId id) {
        return warehouseRepository.takeCopy(id);
    }
}
