package com.pivovarit.summaries.domain;

interface OutboxEvent {
    String getType();
}
