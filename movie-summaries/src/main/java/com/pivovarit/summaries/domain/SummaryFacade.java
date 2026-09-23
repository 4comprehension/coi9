package com.pivovarit.summaries.domain;

import java.time.Instant;
import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;
    private final OutboxRepository outboxRepository;
    private final TransactionRunner transactionRunner;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository, OutboxRepository outboxRepository, TransactionRunner transactionRunner) {
        this.movieSummaryRepository = movieSummaryRepository;
        this.outboxRepository = outboxRepository;
        this.transactionRunner = transactionRunner;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }

    public boolean createOrUpdate(long movieId, String summary) {
        var event = new MovieSummaryUpdatedEvent(movieId, summary, Instant.now());

        return transactionRunner.inTransaction(context -> {
            boolean created = movieSummaryRepository.upsert(context, movieId, summary);
            outboxRepository.save(context, event);
            return created;
        });
    }
}
