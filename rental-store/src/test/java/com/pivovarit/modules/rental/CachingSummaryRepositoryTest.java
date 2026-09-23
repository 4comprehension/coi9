package com.pivovarit.modules.rental;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CachingSummaryRepositoryTest {

    private final CachingSummaryRepository repository = new CachingSummaryRepository(id -> Optional.empty());

    @Test
    void shouldApplyNewerVersion() {
        repository.updateSummary(1L, 1, "first");
        repository.updateSummary(1L, 2, "second");

        assertThat(repository.getSummary(1L)).contains("second");
    }

    @Test
    void shouldIgnoreOlderVersion() {
        repository.updateSummary(1L, 2, "second");
        repository.updateSummary(1L, 1, "stale");

        assertThat(repository.getSummary(1L)).contains("second");
    }

    @Test
    void shouldIgnoreDuplicateVersion() {
        repository.updateSummary(1L, 2, "second");
        repository.updateSummary(1L, 2, "duplicate");

        assertThat(repository.getSummary(1L)).contains("second");
    }
}
