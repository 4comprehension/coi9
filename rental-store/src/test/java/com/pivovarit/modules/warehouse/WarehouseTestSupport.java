package com.pivovarit.modules.warehouse;

import com.pivovarit.modules.rental.MovieId;

public class WarehouseTestSupport {

    public static WarehouseFacade withCopies(MovieId movieId, int copies) {
        WarehouseFacade facade = new WarehouseFacade(new InMemoryWarehouseRepository());
        facade.restock(movieId, copies);
        return facade;
    }
}
