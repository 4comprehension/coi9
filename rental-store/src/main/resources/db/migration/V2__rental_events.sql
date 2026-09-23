CREATE TABLE rental_events
(
    user_email  TEXT      NOT NULL,
    version     BIGINT    NOT NULL,
    movie_id    BIGINT    NOT NULL,
    event_type  TEXT      NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_email, version)
);
