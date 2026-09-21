package com.pivovarit.movies.service;

import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;
import com.pivovarit.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie findById(int id) {
        return movieRepository.findById(new MovieId(id)).orElseThrow();
    }
}
