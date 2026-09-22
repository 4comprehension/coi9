package com.pivovarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    // 1. /movies oraz /movies/{id} zwracają dodatkowo 'summary' (rozszerzamy MovieDto)
    // 2. SummaryFacade wykorzystujemy w RentalFacade
    // 3. Robimy update unit oraz black-box tests
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
