package com.pivovarit.modules.rental;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

class CachingSummaryRepository implements SummaryRepository {

    private static final Logger log = LoggerFactory.getLogger(CachingSummaryRepository.class);

    private final SummaryRepository delegate;
    private final Cache<Long, Optional<String>> cache;

    CachingSummaryRepository(SummaryRepository delegate) {
        this.delegate = delegate;
        this.cache = Caffeine.newBuilder()
          .maximumSize(1_000)
          .expireAfterWrite(Duration.ofMinutes(10))
          .build();
    }

    @Override
    public Optional<String> getSummary(long movieId) {
        Optional<String> summary;
        try {
            summary = delegate.getSummary(movieId);
        } catch (RuntimeException e) {
            summary = Optional.empty();
        }

        if (summary.isPresent()) {
            cache.put(movieId, summary);
            return summary;
        }

        return cache.getIfPresent(movieId);
    }

    @Override
    public void updateSummary(long movieId, String summary) {
        log.info("refreshing cache for movie id: {}", movieId);
        cache.put(movieId, Optional.of(summary));
    }
}
