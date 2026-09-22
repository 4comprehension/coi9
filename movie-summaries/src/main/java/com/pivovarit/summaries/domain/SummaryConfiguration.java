package com.pivovarit.summaries.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SummaryConfiguration {

    @Bean
    public SummaryFacade summaryFacade() {
        return new SummaryFacade(new StaticMovieSummaryRepository());
    }
}
