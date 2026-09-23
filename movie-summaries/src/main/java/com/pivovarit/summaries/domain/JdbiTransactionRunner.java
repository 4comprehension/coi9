package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Jdbi;

import java.util.function.Function;

// just an example (a bit too much overengineering)
class JdbiTransactionRunner implements TransactionRunner {

    private final Jdbi jdbi;

    JdbiTransactionRunner(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public <T> T inTransaction(Function<TransactionContext, T> work) {
        return jdbi.inTransaction(handle -> work.apply(new JdbiTransactionContext(handle)));
    }
}
