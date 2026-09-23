package com.pivovarit.summaries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieSummariesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieSummariesApplication.class, args);
    }
}
