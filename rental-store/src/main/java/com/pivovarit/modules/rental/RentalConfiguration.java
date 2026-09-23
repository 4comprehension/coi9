package com.pivovarit.modules.rental;

import com.pivovarit.modules.warehouse.WarehouseFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class RentalConfiguration {

    @Bean
    RentalFacade movieService(SummaryRepository summaryRepository, MovieRepository movieRepository,
      RentalEventStore rentalEventStore, WarehouseFacade warehouseFacade) {
        return new RentalFacade(new CachingSummaryRepository(summaryRepository), movieRepository,
          rentalEventStore, warehouseFacade);
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    ExampleRestClientRunner runner(RestClient restClient) {
        return new ExampleRestClientRunner(restClient);
    }
}
