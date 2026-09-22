package com.pivovarit.modules.rental;

import com.pivovarit.modules.rental.api.MovieAddRequest;
import com.pivovarit.modules.rental.api.MovieDto;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RentalFacadeTest {

    @RepeatedTest(1000)
    void shouldAddMovie() {
        RentalFacade service = instance();

        MovieAddRequest movie = new MovieAddRequest(42, "Avengers: Doomsday", "NEW");

        assertThat(service.findAll()).isEmpty();

        service.add(movie);

        var expected = new MovieDto(42, "Avengers: Doomsday", "NEW", "description");

        assertThat(service.findAll())
          .hasSize(1)
          .contains(expected);

        assertThat(service.findById(42))
          .contains(expected);
    }

    public static RentalFacade instance() {
        return new RentalFacade(_ -> Optional.of("description"), new InMemoryMovieRepository());
    }
}
