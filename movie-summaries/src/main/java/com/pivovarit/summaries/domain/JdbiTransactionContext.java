package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;

record JdbiTransactionContext(Handle handle) implements TransactionContext {

    static Handle handleOf(TransactionContext context) {
        if (context instanceof JdbiTransactionContext(Handle handle)) {
            return handle;
        }
        throw new IllegalArgumentException("expected a JdbiTransactionContext, got: " + context);
    }
}
