package com.pivovarit.modules.rental;

import com.pivovarit.modules.rental.api.MovieAddRequest;
import com.pivovarit.modules.rental.api.MovieDto;
import com.pivovarit.modules.warehouse.WarehouseFacade;
import com.pivovarit.modules.warehouse.WarehouseTestSupport;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RentalFacadeTest {

    private static final MovieId TITANIC = new MovieId(1);
    private static final MovieId MATRIX = new MovieId(2);
    private static final MovieId INCEPTION = new MovieId(3);
    private static final MovieId AVATAR = new MovieId(4);
    private static final String ALICE = "alice@example.com";

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

    @Test
    void shouldRentAvailableMovie() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 1);
        RentalFacade service = instance(warehouse);

        service.rent(TITANIC, ALICE);

        assertThat(warehouse.availableCopies(TITANIC)).isZero();
    }

    @Test
    void shouldRejectRentWhenNoCopiesAvailable() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 0);
        RentalFacade service = instance(warehouse);

        assertThatThrownBy(() -> service.rent(TITANIC, ALICE))
          .isInstanceOf(MovieNotAvailableException.class);
    }

    @Test
    void shouldRejectRentingAlreadyRentedMovie() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 2);
        RentalFacade service = instance(warehouse);
        service.rent(TITANIC, ALICE);

        assertThatThrownBy(() -> service.rent(TITANIC, ALICE))
          .isInstanceOf(MovieAlreadyRentedException.class);
    }

    @Test
    void shouldReturnRentedMovieAndRestockWarehouse() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 1);
        RentalFacade service = instance(warehouse);
        service.rent(TITANIC, ALICE);

        service.returnMovie(TITANIC, ALICE);

        assertThat(warehouse.availableCopies(TITANIC)).isEqualTo(1);
    }

    @Test
    void shouldRejectReturningMovieThatWasNeverRented() {
        RentalFacade service = instance(WarehouseTestSupport.withCopies(TITANIC, 1));

        assertThatThrownBy(() -> service.returnMovie(TITANIC, ALICE))
          .isInstanceOf(MovieNotRentedException.class);
    }

    @Test
    void shouldRejectReturningMovieRentedByAnotherUser() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 1);
        RentalFacade service = instance(warehouse);
        service.rent(TITANIC, ALICE);

        assertThatThrownBy(() -> service.returnMovie(TITANIC, "bob@example.com"))
          .isInstanceOf(MovieNotRentedException.class);
    }

    @Test
    void shouldRejectRentingWhenUserReachesActiveRentalLimit() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 1);
        warehouse.restock(MATRIX, 1);
        warehouse.restock(INCEPTION, 1);
        warehouse.restock(AVATAR, 1);
        RentalFacade service = instance(warehouse);
        service.rent(TITANIC, ALICE);
        service.rent(MATRIX, ALICE);
        service.rent(INCEPTION, ALICE);

        assertThatThrownBy(() -> service.rent(AVATAR, ALICE))
          .isInstanceOf(RentalLimitExceededException.class);
    }

    @Test
    void shouldAllowRentingAfterReturningFreesUpASlot() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 1);
        warehouse.restock(MATRIX, 1);
        warehouse.restock(INCEPTION, 1);
        warehouse.restock(AVATAR, 1);
        RentalFacade service = instance(warehouse);
        service.rent(TITANIC, ALICE);
        service.rent(MATRIX, ALICE);
        service.rent(INCEPTION, ALICE);
        service.returnMovie(TITANIC, ALICE);

        service.rent(AVATAR, ALICE);

        assertThat(warehouse.availableCopies(AVATAR)).isZero();
    }

    @Test
    void shouldReplayFlattenedHistoryPerMovie() {
        WarehouseFacade warehouse = WarehouseTestSupport.withCopies(TITANIC, 2);
        warehouse.restock(MATRIX, 1);
        RentalFacade service = instance(warehouse);
        service.rent(TITANIC, ALICE);
        service.returnMovie(TITANIC, ALICE);
        service.rent(TITANIC, ALICE);
        service.rent(MATRIX, ALICE);
        service.returnMovie(MATRIX, ALICE);

        var history = service.replayHistory(ALICE);

        assertThat(history).hasSize(2);
        assertThat(history.get(TITANIC)).isInstanceOf(RentalEvent.MovieRented.class);
        assertThat(history.get(MATRIX)).isInstanceOf(RentalEvent.MovieReturned.class);
    }

    public static RentalFacade instance() {
        return instance(WarehouseTestSupport.withCopies(new MovieId(0), 0));
    }

    public static RentalFacade instance(WarehouseFacade warehouse) {
        return new RentalFacade(new CachingSummaryRepository(_ -> Optional.of("description")),
          new InMemoryMovieRepository(), new InMemoryRentalEventStore(), warehouse);
    }
}
