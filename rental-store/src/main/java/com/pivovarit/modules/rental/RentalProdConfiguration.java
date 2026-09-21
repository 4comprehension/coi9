package com.pivovarit.modules.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
class RentalProdConfiguration {

    @Bean
    MovieRepository jdbiMovieRepository(DataSource dataSource) {
        return new JdbiMovieRepository(dataSource);
    }
}
