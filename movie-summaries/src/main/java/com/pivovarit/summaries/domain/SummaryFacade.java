package com.pivovarit.summaries.domain;

import java.time.Instant;
import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;
    private final SummaryEventPublisher summaryEventPublisher;
    private final OutboxRepository outboxRepository;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository, SummaryEventPublisher summaryEventPublisher, OutboxRepository outboxRepository) {
        this.movieSummaryRepository = movieSummaryRepository;
        this.summaryEventPublisher = summaryEventPublisher;
        this.outboxRepository = outboxRepository;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }

    public boolean createOrUpdate(long movieId, String summary) {
        boolean created = movieSummaryRepository.upsert(movieId, summary);
        var event = new MovieSummaryUpdatedEvent(movieId, summary, Instant.now());
        outboxRepository.save(event);
        summaryEventPublisher.publish(event);
        return created;
    }
}
