package com.pivovarit.modules.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RentalLimitExceededException extends RuntimeException {

    RentalLimitExceededException(String userEmail, int limit) {
        super(userEmail + " already has " + limit + " active rentals");
    }
}
