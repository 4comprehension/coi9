package com.pivovarit.modules.rental;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConcurrentRentalModificationException extends RuntimeException {

    ConcurrentRentalModificationException(String userEmail, long expectedVersion) {
        super("rentals of " + userEmail + " were modified concurrently (expected version " + expectedVersion + ")");
    }
}
