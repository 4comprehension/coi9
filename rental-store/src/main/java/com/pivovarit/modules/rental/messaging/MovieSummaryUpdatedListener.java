package com.pivovarit.modules.rental.messaging;

import com.pivovarit.modules.rental.RentalFacade;
import com.pivovarit.modules.rental.api.MovieSummaryUpdatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class MovieSummaryUpdatedListener {

    private final RentalFacade rentalFacade;

    MovieSummaryUpdatedListener(RentalFacade rentalFacade) {
        this.rentalFacade = rentalFacade;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_SUMMARY_UPDATED)
    void onMovieSummaryUpdated(MovieSummaryUpdatedEvent event) {
        rentalFacade.onMovieSummaryChanged(event);
    }
}
