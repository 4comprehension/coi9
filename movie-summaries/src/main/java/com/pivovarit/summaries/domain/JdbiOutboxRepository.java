package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.util.List;

class JdbiOutboxRepository implements OutboxRepository {

    private final Jdbi jdbi;
    private final ObjectMapper objectMapper;

    JdbiOutboxRepository(DataSource dataSource, ObjectMapper objectMapper) {
        this.jdbi = Jdbi.create(dataSource);
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

    @Override
    public List<OutboxRecord> findAll() {
        return jdbi.withHandle(handle -> handle
          .createQuery("SELECT id, event_type, payload FROM outbox ORDER BY id")
          .map((rs, ctx) -> new OutboxRecord(rs.getLong("id"), rs.getString("event_type"), rs.getString("payload")))
          .list());
    }

    @Override
    public void remove(long id) {
        jdbi.useHandle(handle -> handle
          .createUpdate("DELETE FROM outbox WHERE id = :id")
          .bind("id", id)
          .execute());
    }
}
