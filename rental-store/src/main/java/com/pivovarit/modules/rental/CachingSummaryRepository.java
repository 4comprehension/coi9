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
    private final Cache<Long, VersionedSummary> cache;

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
            cache.put(movieId, new VersionedSummary(0, summary.get()));
            return summary;
        }

        return Optional.ofNullable(cache.getIfPresent(movieId))
          .map(VersionedSummary::summary);
    }

    public void updateSummary(long movieId, long version, String summary) {
        var incoming = new VersionedSummary(version, summary);
        var current = cache.asMap().merge(movieId, incoming,
          (existing, candidate) -> candidate.version() > existing.version() ? candidate : existing);

        if (current == incoming) {
            log.info("refreshing cache for movie id: {}, version: {}", movieId, version);
        } else {
            log.info("ignoring stale summary update for movie id: {}, incoming version: {}, current version: {}", movieId, version, current.version());
        }
    }

    record VersionedSummary(long version, String summary) {
    }
}
