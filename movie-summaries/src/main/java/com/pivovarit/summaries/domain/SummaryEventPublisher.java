package com.pivovarit.summaries.domain;

public interface SummaryEventPublisher {
    void publish(MovieSummaryUpdatedEvent event);
}
