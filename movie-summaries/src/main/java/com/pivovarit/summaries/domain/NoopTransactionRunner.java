package com.pivovarit.summaries.domain;

import java.util.function.Function;

public class NoopTransactionRunner implements TransactionRunner {

    private static final TransactionContext NOOP_CONTEXT = new TransactionContext() {};

    @Override
    public <T> T inTransaction(Function<TransactionContext, T> work) {
        return work.apply(NOOP_CONTEXT);
    }
}
