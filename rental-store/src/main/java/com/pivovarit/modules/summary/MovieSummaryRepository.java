package com.pivovarit.modules.summary;

import java.util.Optional;

public interface MovieSummaryRepository {
    Optional<String> getSummary(long movieId);
}
