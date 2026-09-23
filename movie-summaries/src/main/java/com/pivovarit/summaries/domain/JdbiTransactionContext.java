package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;

record JdbiTransactionContext(Handle handle) implements TransactionContext {
}
