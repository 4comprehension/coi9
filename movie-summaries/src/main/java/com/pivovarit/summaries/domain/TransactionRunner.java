package com.pivovarit.summaries.domain;

import java.util.function.Function;

public interface TransactionRunner {
    <T> T inTransaction(Function<TransactionContext, T> work);
}
