package com.pivovarit.summaries.domain;

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
            public void save(TransactionContext context, OutboxEvent event) {
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

    @Bean
    TransactionRunner noopTransactionRunner() {
        return new NoopTransactionRunner();
    }
}
