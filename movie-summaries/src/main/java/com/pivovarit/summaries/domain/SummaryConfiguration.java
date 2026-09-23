package com.pivovarit.summaries.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class SummaryConfiguration {

    private static final Clock clock = Clock.systemUTC();

    @Bean
    public SummaryFacade summaryFacade(MovieSummaryRepository movieSummaryRepository, OutboxRepository outboxRepository, TransactionRunner transactionRunner) {
        return new SummaryFacade(movieSummaryRepository, outboxRepository, transactionRunner, clock);
    }
}
