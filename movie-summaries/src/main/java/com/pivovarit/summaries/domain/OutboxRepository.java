package com.pivovarit.summaries.domain;

interface OutboxRepository {
    void save(MovieSummaryUpdatedEvent event);
}
