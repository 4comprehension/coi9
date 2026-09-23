package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("dev")
class SummaryDevConfiguration {

    @Bean
    MovieSummaryRepository staticMovieSummaryRepository() {
        return new StaticMovieSummaryRepository();
    }

    @Bean
    OutboxRepository noopOutboxRepository() {
        return new OutboxRepository() {
            @Override
            public void save(Handle handle, OutboxEvent event) {
            }

            @Override
            public List<OutboxRecord> findAll() {
                return List.of();
            }

            @Override
            public void remove(long id) {
            }
        };
    }
}
