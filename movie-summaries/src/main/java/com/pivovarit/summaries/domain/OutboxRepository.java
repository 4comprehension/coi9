package com.pivovarit.summaries.domain;

import java.util.List;

interface OutboxRepository {
    void save(TransactionContext context, OutboxEvent event);

    List<OutboxRecord> findAll();

    void remove(long id);
}
