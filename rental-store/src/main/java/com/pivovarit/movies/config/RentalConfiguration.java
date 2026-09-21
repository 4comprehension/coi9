package com.pivovarit.movies.config;

import com.pivovarit.movies.repository.MovieRepository;
import com.pivovarit.movies.service.MovieService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RentalConfiguration {

    @Bean
    MovieService movieService(MovieRepository movieRepository) {
        return new MovieService(movieRepository);
    }
}
