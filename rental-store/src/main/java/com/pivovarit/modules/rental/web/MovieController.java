package com.pivovarit.modules.rental.web;

import com.pivovarit.modules.rental.MovieId;
import com.pivovarit.modules.rental.api.MovieAddRequest;
import com.pivovarit.modules.rental.api.MovieDto;
import com.pivovarit.modules.rental.RentalFacade;
import com.pivovarit.modules.rental.api.RentalOperationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
class MovieController {

    // https://odrotbohm.de/2013/11/why-field-injection-is-evil/
    private final RentalFacade movieService;

    public MovieController(RentalFacade movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    @GetMapping("/movies")
    public Collection<MovieDto> findAll() {

        return movieService.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable int id) {
        return ResponseEntity.of(movieService.findById(id));
    }

    @PostMapping("/movies")
    public void addMovie(@RequestBody MovieAddRequest newMovie) {
        this.movieService.add(newMovie);
    }

    @PostMapping("/rentals/rent")
    public void rent(@RequestBody RentalOperationRequest request) {
        movieService.rent(new MovieId(request.movieId()), request.email());
    }

    @PostMapping("/rentals/return")
    public void returnMovie(@RequestBody RentalOperationRequest request) {
        movieService.returnMovie(new MovieId(request.movieId()), request.email());
    }
}
