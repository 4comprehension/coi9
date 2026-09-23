package com.pivovarit.summaries.messaging;

import com.pivovarit.summaries.domain.MovieSummaryUpdatedEvent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMqConfig {

    static final String EXCHANGE = "movie-summaries";
    static final String QUEUE_SUMMARY_UPDATED = "movie-summary.updated.queue";

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        return rabbitAdmin;
    }

    @Bean
    TopicExchange movieSummariesExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue movieSummaryUpdatedQueue() {
        return new Queue(QUEUE_SUMMARY_UPDATED);
    }

    @Bean
    Binding movieSummaryUpdatedBinding() {
        return BindingBuilder.bind(movieSummaryUpdatedQueue())
          .to(movieSummariesExchange())
          .with(MovieSummaryUpdatedEvent.ROUTING_KEY);
    }
}
