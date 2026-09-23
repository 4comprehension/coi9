package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;

class JdbiOutboxRepository implements OutboxRepository {

    private final Jdbi jdbi;
    private final ObjectMapper objectMapper;

    JdbiOutboxRepository(DataSource ds, ObjectMapper objectMapper) {
        this.jdbi = Jdbi.create(ds).installPlugin(new PostgresPlugin());
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(MovieSummaryUpdatedEvent event) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("""
                INSERT INTO outbox(event_type, payload) VALUES (:type, CAST(:payload AS JSON))
                """)
              .bind("type", "MovieSummaryUpdatedEvent")
              .bind("payload", objectMapper.writeValueAsString(event))
              .execute();
        });
    }
}
