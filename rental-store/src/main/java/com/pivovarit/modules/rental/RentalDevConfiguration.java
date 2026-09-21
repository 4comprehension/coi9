package com.pivovarit.modules.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
class RentalDevConfiguration {

    @Bean
    MovieRepository inMemoryMovieRepository() {
        return new InMemoryMovieRepository();
    }
}
