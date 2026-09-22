package com.pivovarit.modules.rental;

import com.pivovarit.modules.summary.SummaryFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RentalConfiguration {

    @Bean
    RentalFacade movieService(SummaryFacade summaryFacade, MovieRepository movieRepository) {
        return new RentalFacade(summaryFacade::getSummary, movieRepository);
    }
}
