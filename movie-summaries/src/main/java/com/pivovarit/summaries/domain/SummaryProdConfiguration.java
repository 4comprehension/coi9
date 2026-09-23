package com.pivovarit.summaries.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
class SummaryProdConfiguration {

    @Bean
    MovieSummaryRepository jdbiMovieSummaryRepository(DataSource dataSource) {
        return new JdbiMovieSummaryRepository(dataSource);
    }

    @Bean
    JdbiOutboxRepository jdbiOutboxRepository(DataSource dataSource, ObjectMapper objectMapper) {
        return new JdbiOutboxRepository(dataSource, objectMapper);
    }
}
