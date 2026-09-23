package com.pivovarit.modules.rental;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;

class JdbiRentalEventStore implements RentalEventStore {

    private final Jdbi jdbi;

    JdbiRentalEventStore(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource);
    }

    @Override
    public void append(RentalEvent event, long expectedVersion) {
        int inserted = jdbi.withHandle(handle -> handle
          .createUpdate("""
            INSERT INTO rental_events (user_email, version, movie_id, event_type, occurred_at)
            VALUES (:userEmail, :version, :movieId, :eventType, :occurredAt)
            ON CONFLICT (user_email, version) DO NOTHING
            """)
          .bind("userEmail", event.userEmail())
          .bind("version", expectedVersion + 1)
          .bind("movieId", event.movieId().id())
          .bind("eventType", eventType(event))
          .bind("occurredAt", event.occurredAt())
          .execute());

        if (inserted == 0) {
            throw new ConcurrentRentalModificationException(event.userEmail(), expectedVersion);
        }
    }

    @Override
    public List<RentalEvent> findByUser(String userEmail) {
        return jdbi.withHandle(handle -> handle
          .createQuery("""
            SELECT * FROM rental_events
            WHERE user_email = :userEmail
            ORDER BY version ASC
            """)
          .bind("userEmail", userEmail)
          .map(toEvent())
          .list());
    }

    private static String eventType(RentalEvent event) {
        return switch (event) {
            case RentalEvent.MovieRented ignored -> "MOVIE_RENTED";
            case RentalEvent.MovieReturned ignored -> "MOVIE_RETURNED";
        };
    }

    private static RowMapper<RentalEvent> toEvent() {
        return (rs, _) -> {
            MovieId movieId = new MovieId(rs.getLong("movie_id"));
            String userEmail = rs.getString("user_email");
            Instant occurredAt = rs.getTimestamp("occurred_at").toInstant();

            return switch (rs.getString("event_type")) {
                case "MOVIE_RENTED" -> new RentalEvent.MovieRented(movieId, userEmail, occurredAt);
                case "MOVIE_RETURNED" -> new RentalEvent.MovieReturned(movieId, userEmail, occurredAt);
                case String other -> throw new IllegalStateException("unknown rental event type: " + other);
            };
        };
    }
}
