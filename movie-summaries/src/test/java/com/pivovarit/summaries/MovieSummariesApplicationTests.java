package com.pivovarit.summaries;

import com.pivovarit.summaries.domain.SummaryFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieSummariesApplicationTests {

    @Autowired
    private SummaryFacade summaries;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldReturnSummaryForKnownMovie() {
        var summary = summaries.getSummary(1L);

        assertThat(summary).isPresent();
    }
}
