package com.pivovarit.modules.summary;

import java.util.Optional;

public final class SummaryFacade {

    private final MovieSummaryRepository movieSummaryRepository;

    public SummaryFacade(MovieSummaryRepository movieSummaryRepository) {
        this.movieSummaryRepository = movieSummaryRepository;
    }

    public Optional<String> getSummary(long movieId) {
        return movieSummaryRepository.getSummary(movieId);
    }
}
