package com.pivovarit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    // 1. rozszerzyć HelloScheduler do pełoprawnego event relaya
    // 2. powinien w pętli znajdywać eventy w outboxie, wrzucać je do rabbita, i potem usuwać z outboxa
    // 3. usuwamy dodatkowe ręczne wysyłanie eventów
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
