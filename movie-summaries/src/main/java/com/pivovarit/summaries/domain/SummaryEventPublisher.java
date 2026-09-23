package com.pivovarit.summaries.domain;

public interface SummaryEventPublisher {
    void publish(String type, String payload);
}
