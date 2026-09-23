package com.pivovarit.modules.rental;

import com.pivovarit.modules.rental.api.MovieAddRequest;
import com.pivovarit.modules.rental.api.MovieDto;
import com.pivovarit.modules.rental.api.MovieSummaryUpdatedEvent;
import com.pivovarit.modules.warehouse.WarehouseFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class RentalFacade {

    private static final Logger log = LoggerFactory.getLogger(RentalFacade.class);
    private static final int MAX_ACTIVE_RENTALS = 3;

    private final CachingSummaryRepository summaries;
    private final MovieRepository movieRepository;
    private final RentalEventStore rentalEventStore;
    private final WarehouseFacade warehouseFacade;

    RentalFacade(CachingSummaryRepository summaries, MovieRepository movieRepository,
      RentalEventStore rentalEventStore, WarehouseFacade warehouseFacade) {
        this.summaries = summaries;
        this.movieRepository = movieRepository;
        this.rentalEventStore = rentalEventStore;
        this.warehouseFacade = warehouseFacade;
    }

    public void rent(MovieId movieId, String userEmail) {
        List<RentalEvent> history = rentalEventStore.findByUser(userEmail);
        Set<MovieId> currentRentals = currentlyRentedMovies(history);
        if (currentRentals.contains(movieId)) {
            throw new MovieAlreadyRentedException(movieId, userEmail);
        }
        if (currentRentals.size() >= MAX_ACTIVE_RENTALS) {
            throw new RentalLimitExceededException(userEmail, MAX_ACTIVE_RENTALS);
        }

        if (!warehouseFacade.reserve(movieId)) {
            throw new MovieNotAvailableException(movieId);
        }
        try {
            rentalEventStore.append(new RentalEvent.MovieRented(movieId, userEmail, Instant.now()), history.size());
        } catch (RuntimeException e) {
            warehouseFacade.restock(movieId, 1);
            throw e;
        }
    }

    public void returnMovie(MovieId movieId, String userEmail) {
        List<RentalEvent> history = rentalEventStore.findByUser(userEmail);
        if (!currentlyRentedMovies(history).contains(movieId)) {
            throw new MovieNotRentedException(movieId, userEmail);
        }
        rentalEventStore.append(new RentalEvent.MovieReturned(movieId, userEmail, Instant.now()), history.size());
        warehouseFacade.restock(movieId, 1);
    }

    private static Set<MovieId> currentlyRentedMovies(List<RentalEvent> history) {
        Set<MovieId> rentedMovies = new LinkedHashSet<>();
        lastEventByMovie(history).forEach((movieId, lastEvent) -> {
            if (lastEvent instanceof RentalEvent.MovieRented) {
                rentedMovies.add(movieId);
            }
        });
        return rentedMovies;
    }

    Map<MovieId, RentalEvent> replayHistory(String userEmail) {
        return lastEventByMovie(rentalEventStore.findByUser(userEmail));
    }

    private static Map<MovieId, RentalEvent> lastEventByMovie(List<RentalEvent> history) {
        Map<MovieId, RentalEvent> lastEventByMovie = new LinkedHashMap<>();
        for (RentalEvent event : history) {
            lastEventByMovie.put(event.movieId(), event);
        }
        return lastEventByMovie;
    }

    public void printHistory(String userEmail) {
        replayHistory(userEmail).forEach((movieId, lastEvent) ->
          log.info("movieId={} status={} at={}", movieId.id(), status(lastEvent), lastEvent.occurredAt()));
    }

    private static String status(RentalEvent event) {
        return switch (event) {
            case RentalEvent.MovieRented ignored -> "RENTED";
            case RentalEvent.MovieReturned ignored -> "RETURNED";
        };
    }

    public Optional<MovieDto> findById(int id) {
        return movieRepository.findById(new MovieId(id))
          .map(toDto());
    }

    public void add(MovieAddRequest newMovie) {
        movieRepository.save(new Movie(new MovieId(newMovie.id()), newMovie.title(), MovieType.valueOf(newMovie.type())));
    }

    public Collection<MovieDto> findAll() {
        return movieRepository.findAll().stream().map(toDto()).toList();
    }

    public void onMovieSummaryChanged(MovieSummaryUpdatedEvent event) {
        log.info("received summary update for movieId={}", event.movieId());
        summaries.updateSummary(event.movieId(), event.version(), event.summary());
    }

    private Function<Movie, MovieDto> toDto() {
        return m -> {
            String summary = summaries.getSummary(m.id().id()).orElse(null);

            return new MovieDto(m.id().id(), m.title(), m.type().toString(), summary);
        };
    }
}
