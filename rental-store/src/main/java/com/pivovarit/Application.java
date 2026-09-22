package com.pivovarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    // 1. tworzymy nowy serwis: movie-summaries z jedną operacją: HTTP GET /summaries/{id}
    // schemat odpowiedz: {"summary": "lorem ipsum"}
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
