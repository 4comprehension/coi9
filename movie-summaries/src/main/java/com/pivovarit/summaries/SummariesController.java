package com.pivovarit.summaries;

import com.pivovarit.summaries.domain.SummaryFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
class SummariesController {

    private final SummaryFacade summaries;

    SummariesController(SummaryFacade summaries) {
        this.summaries = summaries;
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    @GetMapping("/summaries/{id}")
    public ResponseEntity<SummaryResponse> findById(@PathVariable int id) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            return ResponseEntity.of(summaries.getSummary(id).map(SummaryResponse::new));
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    @PostMapping("/summaries/{id}")
    public ResponseEntity<Void> createOrUpdate(@PathVariable int id, @RequestBody CreateOrUpdateRequest request) {
        return switch (summaries.createOrUpdate(id, request.summary())) {
            case CREATED -> ResponseEntity.status(201).build();
            case UPDATED -> ResponseEntity.ok().build();
            case CONFLICT -> ResponseEntity.status(409).build();
        };
    }

    record SummaryResponse(String summary) {
    }

    record CreateOrUpdateRequest(String summary) {
    }
}
