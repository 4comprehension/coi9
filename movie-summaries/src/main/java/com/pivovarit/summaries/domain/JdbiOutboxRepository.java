package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;
import tools.jackson.databind.ObjectMapper;

class JdbiOutboxRepository implements OutboxRepository {

    private final ObjectMapper objectMapper;

    JdbiOutboxRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(Handle handle, OutboxEvent event) {
        handle.createUpdate("""
            INSERT INTO outbox(event_type, payload) VALUES (:type, CAST(:payload AS JSON))
            """)
          .bind("type", event.getType())
          .bind("payload", objectMapper.writeValueAsString(event))
          .execute();
    }
}
