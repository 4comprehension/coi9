package com.pivovarit.summaries.domain;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
class SummaryConfiguration {

    @Bean
    public SummaryFacade summaryFacade(MovieSummaryRepository movieSummaryRepository, OutboxRepository outboxRepository, @Nullable DataSource dataSource) {
        return new SummaryFacade(movieSummaryRepository, outboxRepository, dataSource);
    }
}
