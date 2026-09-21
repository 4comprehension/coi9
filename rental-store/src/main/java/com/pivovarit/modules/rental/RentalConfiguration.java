package com.pivovarit.modules.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RentalConfiguration {

    @Bean
    RentalFacade movieService(MovieRepository movieRepository) {
        return new RentalFacade(movieRepository);
    }
}
