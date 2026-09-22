package com.pivovarit.summaries.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
class SummaryProdConfiguration {

    @Bean
    MovieSummaryRepository jdbiMovieSummaryRepository(DataSource dataSource) {
        return new JdbiMovieSummaryRepository(dataSource);
    }
}
