CREATE TABLE outbox
(
    id           SERIAL PRIMARY KEY,
    event_type   TEXT                     NOT NULL,
    payload      JSON                     NOT NULL,
    persisted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
