package com.pivovarit.summaries.domain;

import java.time.Instant;
import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;
    private final SummaryEventPublisher summaryEventPublisher;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository, SummaryEventPublisher summaryEventPublisher) {
        this.movieSummaryRepository = movieSummaryRepository;
        this.summaryEventPublisher = summaryEventPublisher;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }

    public boolean createOrUpdate(long movieId, String summary) {
        boolean created = movieSummaryRepository.upsert(movieId, summary);
        summaryEventPublisher.publish(new MovieSummaryUpdatedEvent(movieId, summary, Instant.now()));
        return created;
    }
}
