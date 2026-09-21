package com.pivovarit.movies;

import com.pivovarit.movies.repository.InMemoryMovieRepository;
import com.pivovarit.movies.repository.JdbiMovieRepository;
import com.pivovarit.movies.repository.MovieRepository;
import com.pivovarit.movies.service.MovieService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RentalConfiguration {

    @Bean
    @ConditionalOnProperty(name = "rental.repository", havingValue = "in-memory", matchIfMissing = true)
    MovieRepository inMemoryMovieRepository() {
        return new InMemoryMovieRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "rental.repository", havingValue = "jdbi")
    MovieRepository jdbiMovieRepository(DataSource dataSource) {
        return new JdbiMovieRepository(dataSource);
    }

    @Bean
    MovieService movieService(MovieRepository movieRepository) {
        return new MovieService(movieRepository);
    }
}
