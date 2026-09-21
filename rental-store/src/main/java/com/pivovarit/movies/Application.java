package com.pivovarit.movies;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    public static class Runner implements ApplicationRunner {

        private final DataSource ds;

        Runner(DataSource ds) {
            this.ds = ds;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {

        }
    }
}
