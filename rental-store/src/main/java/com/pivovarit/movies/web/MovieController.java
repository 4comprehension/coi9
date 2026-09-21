package com.pivovarit.movies.web;

import com.pivovarit.movies.api.MovieAddRequest;
import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
class MovieController {

    // https://odrotbohm.de/2013/11/why-field-injection-is-evil/
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public Collection<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        return ResponseEntity.of(movieService.findById(id));
    }

    @PostMapping("/movies")
    public void addMovie(@RequestBody MovieAddRequest newMovie) {
        this.movieService.add(newMovie);
    }
}
