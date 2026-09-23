package com.pivovarit.modules.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MovieAlreadyRentedException extends RuntimeException {

    MovieAlreadyRentedException(MovieId movieId, String userEmail) {
        super("movie id=" + movieId.id() + " is already rented by " + userEmail);
    }
}
