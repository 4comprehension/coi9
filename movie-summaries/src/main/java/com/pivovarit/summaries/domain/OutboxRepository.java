package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;

import java.util.List;

interface OutboxRepository {
    void save(Handle handle, OutboxEvent event);

    List<OutboxRecord> findAll();

    void remove(long id);
}
