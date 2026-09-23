package com.pivovarit.summaries.domain;

interface OutboxRepository {
    void save(OutboxEvent event);
}
