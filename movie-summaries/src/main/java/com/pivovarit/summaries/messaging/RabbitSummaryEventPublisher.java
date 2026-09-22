package com.pivovarit.summaries.messaging;

import com.pivovarit.summaries.domain.MovieSummaryUpdatedEvent;
import com.pivovarit.summaries.domain.SummaryEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class RabbitSummaryEventPublisher implements SummaryEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    RabbitSummaryEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(MovieSummaryUpdatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY_SUMMARY_UPDATED, event);
    }
}
