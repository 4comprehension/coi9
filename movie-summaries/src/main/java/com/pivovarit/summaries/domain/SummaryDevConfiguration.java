package com.pivovarit.summaries.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
class SummaryDevConfiguration {

    @Bean
    MovieSummaryRepository staticMovieSummaryRepository() {
        return new StaticMovieSummaryRepository();
    }
}
