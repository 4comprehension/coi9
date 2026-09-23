package com.pivovarit.summaries.domain;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;
    private final OutboxRepository outboxRepository;
    private final TransactionRunner transactionRunner;
    private final Clock clock;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository, OutboxRepository outboxRepository, TransactionRunner transactionRunner, Clock clock) {
        this.movieSummaryRepository = movieSummaryRepository;
        this.outboxRepository = outboxRepository;
        this.transactionRunner = transactionRunner;
        this.clock = clock;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }

    public CreateOrUpdateResult createOrUpdate(long movieId, String summary) {
        return transactionRunner.inTransaction(context -> {
            Optional<Long> version = movieSummaryRepository.upsert(context, movieId, summary);

            if (version.isEmpty()) {
                return CreateOrUpdateResult.CONFLICT;
            }

            var event = new MovieSummaryUpdatedEvent(movieId, summary, version.get(), Instant.now(clock));
            outboxRepository.save(context, event);

            return version.get() == 1 ? CreateOrUpdateResult.CREATED : CreateOrUpdateResult.UPDATED;
        });
    }

    public enum CreateOrUpdateResult {
        CREATED, UPDATED, CONFLICT
    }
}
