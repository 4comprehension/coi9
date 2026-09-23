package com.pivovarit.modules.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class RentalConfiguration {

    @Bean
    RentalFacade movieService(SummaryRepository summaryRepository, MovieRepository movieRepository) {
        return new RentalFacade(new CachingSummaryRepository(summaryRepository), movieRepository);
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
