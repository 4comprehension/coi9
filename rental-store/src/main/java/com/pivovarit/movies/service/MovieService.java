package com.pivovarit.movies.service;

import com.pivovarit.movies.api.MovieAddRequest;
import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;
import com.pivovarit.movies.domain.MovieType;
import com.pivovarit.movies.repository.MovieRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Optional<Movie> findById(int id) {
        return movieRepository.findById(new MovieId(id));
    }

    public void add(MovieAddRequest newMovie) {
        movieRepository.save(new Movie(new MovieId(newMovie.id()), newMovie.title(), MovieType.valueOf(newMovie.type())));
    }

    public Collection<Movie> findAll() {
        return movieRepository.findAll();
    }
}
