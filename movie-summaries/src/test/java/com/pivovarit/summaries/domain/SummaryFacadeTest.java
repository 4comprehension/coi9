package com.pivovarit.summaries.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SummaryFacadeTest {

    private final List<MovieSummaryUpdatedEvent> publishedEvents = new ArrayList<>();
    private final SummaryEventPublisher publisher = publishedEvents::add;
    private final SummaryFacade summaryFacade = new SummaryFacade(new StaticMovieSummaryRepository(), publisher, _ -> {});

    @Test
    void shouldPublishEventWhenSummaryIsCreatedOrUpdated() {
        summaryFacade.createOrUpdate(1L, "an updated summary");

        assertThat(publishedEvents)
          .singleElement()
          .satisfies(event -> {
              assertThat(event.movieId()).isEqualTo(1L);
              assertThat(event.summary()).isEqualTo("an updated summary");
          });
    }
}
