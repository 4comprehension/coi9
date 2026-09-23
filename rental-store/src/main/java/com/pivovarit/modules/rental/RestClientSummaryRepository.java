package com.pivovarit.modules.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.Optional;

class RestClientSummaryRepository implements SummaryRepository {

    private static final Logger log = LoggerFactory.getLogger(RestClientSummaryRepository.class);

    private final RestClient restClient;

    RestClientSummaryRepository(RestClient.Builder restClientBuilder, URI uri) {
        this.restClient = restClientBuilder.baseUrl(uri).build();
    }

    @Override
    public Optional<String> getSummary(long movieId) {
        try {
            var response = restClient.get()
              .uri("/summaries/{id}", movieId)
              .retrieve()
              .body(SummaryResponse.class);

            return Optional.ofNullable(response).map(SummaryResponse::summary);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("failed to fetch summary for movieId={}", movieId, e);
            return Optional.empty();
        }
    }

    record SummaryResponse(String summary) {
    }
}
