package com.pivovarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    // 1. dodać tabelę outbox do movie-summaries, która będzie robiła za 'skrzynkę nadawczą' z eventami
    // checkpoint (dyskutujemy)
    // 2. zapis w jednej tx do tabeli z opisami oraz outbox (+ blackbox test)
    // checkpoint (dyskutujemy)
    // 3. event processor, który wyłapuje eventy i wrzuca do rabbitmq
    // checkpoint (dyskutujemy)
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
