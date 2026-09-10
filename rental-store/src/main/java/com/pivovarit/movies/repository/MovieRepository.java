package com.pivovarit.movies.repository;

import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;
import com.pivovarit.movies.domain.MovieType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class MovieRepository {

    public MovieId save(Movie movie) {
        return new MovieId(42);
    }

    public Collection<Movie> findAll() {
        return Arrays.asList(new Movie(new MovieId(42), "foo", MovieType.NEW));
    }

    public Optional<Movie> findByTitle(String title) {
        return Optional.of(new Movie(new MovieId(42), "foo", MovieType.NEW));
    }

    public Optional<Movie> findById(MovieId id) {
        return Optional.of(new Movie(new MovieId(42), "foo", MovieType.NEW));
    }
}
