package com.pivovarit.movies.service;

import com.pivovarit.movies.api.MovieAddRequest;
import com.pivovarit.movies.domain.Movie;
import com.pivovarit.movies.domain.MovieId;
import com.pivovarit.movies.domain.MovieType;
import com.pivovarit.movies.repository.InMemoryMovieRepository;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MovieServiceTest {

    @RepeatedTest(1000)
    void shouldAddMovie() throws Exception {
        MovieService service = instance();

        MovieAddRequest movie = new MovieAddRequest(42, "Avengers: Doomsday", "NEW");

        assertThat(service.findAll()).isEmpty();

        service.add(movie);

        var expected = new Movie(new MovieId(42), "Avengers: Doomsday", MovieType.NEW);

        assertThat(service.findAll())
          .hasSize(1)
          .contains(expected);

        assertThat(service.findById(42))
          .contains(expected);
    }

    public static MovieService instance() {
        return new MovieService(new InMemoryMovieRepository());
    }
}
