package com.pivovarit.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    // 1. HTTP POST /movies
    // 2. implement InMemoryMovieRepository
    // 3. wire it up and see it work
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
