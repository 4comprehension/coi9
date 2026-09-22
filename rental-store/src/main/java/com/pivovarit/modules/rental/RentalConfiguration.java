package com.pivovarit.modules.rental;

import com.pivovarit.modules.summary.SummaryFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class RentalConfiguration {

    @Bean
    RentalFacade movieService(SummaryFacade summaryFacade, MovieRepository movieRepository) {
        return new RentalFacade(summaryFacade::getSummary, movieRepository);
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
