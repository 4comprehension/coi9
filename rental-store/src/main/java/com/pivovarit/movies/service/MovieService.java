package com.pivovarit.movies.service;

import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;
import com.pivovarit.movies.repository.MovieRepository;

public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie findById(int id) {
        return movieRepository.findById(new MovieId(id)).orElseThrow();
    }
}
