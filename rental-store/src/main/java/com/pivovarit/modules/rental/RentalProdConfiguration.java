package com.pivovarit.modules.rental;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("prod")
class RentalProdConfiguration {

    @Bean
    MovieRepository jdbiMovieRepository(DataSource dataSource) {
        return new JdbiMovieRepository(dataSource);
    }

    @Bean
    RentalEventStore jdbiRentalEventStore(DataSource dataSource) {
        return new JdbiRentalEventStore(dataSource);
    }

    @Bean
    RestClientSummaryRepository restClientSummaryRepository(
      RestClient.Builder restClientBuilder,
      @Value("${service.summaries.url}") URI uri) {
        return new RestClientSummaryRepository(restClientBuilder, uri);
    }

    @Bean
    @Primary
    SummaryRepository cachingSummaryRepository(RestClientSummaryRepository restClientSummaryRepository) {
        return new CachingSummaryRepository(restClientSummaryRepository);
    }
}
