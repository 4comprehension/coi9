package com.pivovarit.modules.rental;

import java.util.Collection;
import java.util.Optional;

interface MovieRepository {
    MovieId save(Movie movie);
    Collection<Movie> findAll();
    Optional<Movie> findByTitle(String title);
    Optional<Movie> findById(MovieId id);
}
