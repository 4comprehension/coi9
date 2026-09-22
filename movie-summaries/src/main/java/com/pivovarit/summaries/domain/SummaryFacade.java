package com.pivovarit.summaries.domain;

import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository) {
        this.movieSummaryRepository = movieSummaryRepository;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }

    public boolean createOrUpdate(long movieId, String summary) {
        return movieSummaryRepository.upsert(movieId, summary);
    }
}
