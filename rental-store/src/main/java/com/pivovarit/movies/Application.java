package com.pivovarit.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    // 1. autowire the whole thing
    // 2. GET /movies/{id}
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
