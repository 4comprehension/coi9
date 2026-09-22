package com.pivovarit.modules.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@Profile("dev")
class RentalDevConfiguration {

    @Bean
    SummaryRepository inMemorySummaryRepository() {
        return new SummaryRepository() {
            @Override
            public Optional<String> getSummary(long movieId) {
                return Optional.of("");
            }

            @Override
            public void updateSummary(long movieId, String summary) {
            }
        };
    }

    @Bean
    MovieRepository inMemoryMovieRepository() {
        return new InMemoryMovieRepository();
    }
}
