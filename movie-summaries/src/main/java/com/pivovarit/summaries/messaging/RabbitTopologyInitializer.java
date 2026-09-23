package com.pivovarit.summaries.messaging;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// prod-only: dev/test contexts have no real broker to eagerly connect to.
@Component
@Profile("prod")
class RabbitTopologyInitializer {

    private final RabbitAdmin rabbitAdmin;

    RabbitTopologyInitializer(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @EventListener(ApplicationReadyEvent.class)
    void declareTopologyOnStartup() {
        rabbitAdmin.initialize();
    }
}
