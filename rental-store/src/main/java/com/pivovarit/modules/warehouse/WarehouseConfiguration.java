package com.pivovarit.modules.warehouse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class WarehouseConfiguration {

    @Bean
    WarehouseFacade warehouseFacade() {
        return new WarehouseFacade(new InMemoryWarehouseRepository());
    }
}
