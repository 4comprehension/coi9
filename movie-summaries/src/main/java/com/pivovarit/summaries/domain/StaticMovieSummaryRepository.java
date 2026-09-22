package com.pivovarit.summaries.domain;

import java.util.Map;
import java.util.Optional;

public class StaticMovieSummaryRepository implements MovieSummaryRepository {

    private static final Map<Long, String> SUMMARIES = Map.of(
      1L, "A teenager bitten by a radioactive spider gains extraordinary powers and must learn to use them responsibly to protect New York City.",
      2L, "Earth's mightiest heroes reunite to face a cataclysmic threat that puts the entire multiverse on the brink of destruction.",
      3L, "In wartime Casablanca, a cynical nightclub owner must decide whether to help his former lover and her fugitive husband escape the Nazis.",
      42L, "A skilled thief who steals secrets through dream-sharing technology is given a chance to have his criminal history erased by planting an idea into a target's subconscious."
    );

    @Override
    public Optional<String> getSummary(long movieId) {
        return Optional.ofNullable(SUMMARIES.get(movieId));
    }
}
