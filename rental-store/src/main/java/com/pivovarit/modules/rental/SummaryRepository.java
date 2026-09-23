package com.pivovarit.modules.rental;

import java.util.Optional;

interface SummaryRepository {
    Optional<String> getSummary(long movieId);
}
