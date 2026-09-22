package com.pivovarit.modules.rental;

import com.pivovarit.modules.rental.api.MovieAddRequest;
import com.pivovarit.modules.rental.api.MovieDto;
import com.pivovarit.modules.summary.SummaryFacade;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class RentalFacade {

    private final SummaryFacade summaries;
    private final MovieRepository movieRepository;

    RentalFacade(SummaryFacade summaries, MovieRepository movieRepository) {
        this.summaries = summaries;
        this.movieRepository = movieRepository;
    }

    public Optional<MovieDto> findById(int id) {
        return movieRepository.findById(new MovieId(id))
          .map(toDto());
    }

    public void add(MovieAddRequest newMovie) {
        movieRepository.save(new Movie(new MovieId(newMovie.id()), newMovie.title(), MovieType.valueOf(newMovie.type())));
    }

    public Collection<MovieDto> findAll() {
        return movieRepository.findAll().stream().map(toDto()).toList();
    }

    private Function<Movie, MovieDto> toDto() {
        return m -> {
            String summary = summaries.getSummary(m.id().id()).orElse(null);

            return new MovieDto(m.id().id(), m.title(), m.type().toString(), summary);
        };
    }
}
