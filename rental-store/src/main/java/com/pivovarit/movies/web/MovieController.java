package com.pivovarit.movies.web;

import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
class MovieController {

    // https://odrotbohm.de/2013/11/why-field-injection-is-evil/
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies/{id}")
    public Movie getMovieById(@PathVariable int id) {
        return movieService.findById(id);
    }
}
