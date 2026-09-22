package com.pivovarit.summaries;

import com.pivovarit.summaries.domain.SummaryFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
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
