package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;
    private final OutboxRepository outboxRepository;
    private final Jdbi jdbi;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository, OutboxRepository outboxRepository, DataSource ds) {
        this.movieSummaryRepository = movieSummaryRepository;
        this.outboxRepository = outboxRepository;
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
