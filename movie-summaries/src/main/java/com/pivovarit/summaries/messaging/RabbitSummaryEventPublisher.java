package com.pivovarit.summaries.messaging;

import com.pivovarit.summaries.domain.SummaryEventPublisher;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
class RabbitSummaryEventPublisher implements SummaryEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    RabbitSummaryEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String type, String payload) {
        var message = MessageBuilder.withBody(payload.getBytes(StandardCharsets.UTF_8))
          .setContentType(MessageProperties.CONTENT_TYPE_JSON)
          .build();
        rabbitTemplate.send(RabbitMqConfig.EXCHANGE, type, message);
    }
}
