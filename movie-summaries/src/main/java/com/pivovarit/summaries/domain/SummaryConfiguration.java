package com.pivovarit.summaries.domain;

import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;

@Configuration
class SummaryConfiguration {

    @Bean
    public SummaryFacade summaryFacade(MovieSummaryRepository movieSummaryRepository, SummaryEventPublisher summaryEventPublisher, OutboxRepository outboxRepository, ObjectMapper objectMapper, @Nullable DataSource dataSource) {
        return new SummaryFacade(movieSummaryRepository, summaryEventPublisher, outboxRepository, objectMapper, dataSource);
    }
}
