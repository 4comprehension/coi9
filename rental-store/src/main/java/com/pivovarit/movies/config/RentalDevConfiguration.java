package com.pivovarit.movies.config;

import com.pivovarit.movies.repository.InMemoryMovieRepository;
import com.pivovarit.movies.repository.MovieRepository;
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
