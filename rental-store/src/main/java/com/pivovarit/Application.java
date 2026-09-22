package com.pivovarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 1. stworzyć nową implementację SummaryRepository, która strzela do serwisu z opisami (tylko profil prod)
// 2. stworzyć nową implementację stub SummaryRepository, która wykorzystywana jest tylko na profilu 'dev', niech zwraca na sztywno pustego stringa
// 3. URL do drugiego serwisu musi być przekazywany przez property/ENV
// 4. ignorujemy bb testy
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
