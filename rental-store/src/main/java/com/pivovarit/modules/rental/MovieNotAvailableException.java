package com.pivovarit.modules.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MovieNotAvailableException extends RuntimeException {

    MovieNotAvailableException(MovieId movieId) {
        super("no copies available for movie id=" + movieId.id());
    }
}
