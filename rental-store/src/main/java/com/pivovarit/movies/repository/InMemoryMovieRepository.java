package com.pivovarit.movies.repository;

import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMovieRepository implements MovieRepository {

    private final Map<MovieId, Movie> movies = new ConcurrentHashMap<>();

    @Override
    public MovieId save(Movie movie) {
        return null;
    }

    @Override
    public Collection<Movie> findAll() {
        return List.of();
    }

    @Override
    public Optional<Movie> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public Optional<Movie> findById(MovieId id) {
        return Optional.empty();
    }
}
