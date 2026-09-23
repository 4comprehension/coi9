package com.pivovarit.summaries.domain;

import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SummaryFacadeTest {

    private record PublishedEvent(String type, String payload) {
    }

    private final List<PublishedEvent> publishedEvents = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SummaryFacade summaryFacade = new SummaryFacade(new StaticMovieSummaryRepository(), new OutboxRepository() {
        @Override
        public void save(Handle handle, OutboxEvent event) {

        }

        @Override
        public List<OutboxRecord> findAll() {
            return List.of();
        }

        @Override
        public void remove(long id) {

        }
    }, null);

    @Test
    @Disabled
    void shouldPublishEventWhenSummaryIsCreatedOrUpdated() {
        summaryFacade.createOrUpdate(1L, "an updated summary");

        assertThat(publishedEvents)
          .singleElement()
          .satisfies(published -> {
              assertThat(published.type()).isEqualTo(MovieSummaryUpdatedEvent.ROUTING_KEY);
              var event = objectMapper.readValue(published.payload(), MovieSummaryUpdatedEvent.class);
              assertThat(event.movieId()).isEqualTo(1L);
              assertThat(event.summary()).isEqualTo("an updated summary");
          });
    }
}
