package com.pivovarit.summaries.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SummaryConfiguration {

    @Bean
    public SummaryFacade summaryFacade(MovieSummaryRepository movieSummaryRepository, OutboxRepository outboxRepository, TransactionRunner transactionRunner) {
        return new SummaryFacade(movieSummaryRepository, outboxRepository, transactionRunner);
    }
}
