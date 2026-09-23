package com.pivovarit.summaries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class HelloScheduler {

    private static final Logger log = LoggerFactory.getLogger(HelloScheduler.class);

    @Scheduled(fixedRate = 5000)
    void sayHello() {
        log.info("hello");
    }
}
