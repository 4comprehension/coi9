package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import tools.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;
    private final SummaryEventPublisher summaryEventPublisher;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final Jdbi jdbi;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository, SummaryEventPublisher summaryEventPublisher, OutboxRepository outboxRepository, ObjectMapper objectMapper, DataSource ds) {
        this.movieSummaryRepository = movieSummaryRepository;
        this.summaryEventPublisher = summaryEventPublisher;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
        this.jdbi = ds != null ? Jdbi.create(ds).installPlugin(new PostgresPlugin()) : null;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }

    public boolean createOrUpdate(long movieId, String summary) {
        var event = new MovieSummaryUpdatedEvent(movieId, summary, Instant.now());

        if (jdbi != null) {
            // published by OutboxRelay once the write commits — no direct publish here
            return jdbi.inTransaction(handle -> {
                boolean created = movieSummaryRepository.upsert(handle, movieId, summary);
                outboxRepository.save(handle, event);
                return created;
            });
        }

        return movieSummaryRepository.upsert(movieId, summary);
    }
}
