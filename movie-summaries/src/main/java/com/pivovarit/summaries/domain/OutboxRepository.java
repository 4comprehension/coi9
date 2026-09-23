package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;

interface OutboxRepository {
    void save(Handle handle, OutboxEvent event);
}
