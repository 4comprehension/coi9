package com.pivovarit.summaries;

import com.pivovarit.summaries.domain.SummaryFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SummariesController {

    private final SummaryFacade summaries;

    SummariesController(SummaryFacade summaries) {
        this.summaries = summaries;
    }

    @GetMapping("/summaries/{id}")
    public ResponseEntity<SummaryResponse> findById(@PathVariable int id) {
        return ResponseEntity.of(summaries.getSummary(id).map(SummaryResponse::new));
    }

    record SummaryResponse(String summary) {
    }
}
