package com.pivovarit.summaries.domain;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class OutboxRelay {

    private final OutboxRepository outboxRepository;
    private final SummaryEventPublisher eventPublisher;

    OutboxRelay(OutboxRepository outboxRepository, SummaryEventPublisher eventPublisher) {
        this.outboxRepository = outboxRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 1000)
    void handle() {
        for (OutboxRecord event : outboxRepository.findAll()) {
            eventPublisher.publish(event.type(), event.payload());
            outboxRepository.remove(event.id());
        }
    }
}
