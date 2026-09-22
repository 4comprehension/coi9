package com.pivovarit.modules.rental;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Map;

class ExampleRestClientRunner implements ApplicationRunner {

    private final RestClient restClient;

    ExampleRestClientRunner(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        HttpBinResponse body = restClient.get()
          .uri(URI.create("https://httpbin.org/get"))
          .retrieve()
          .body(HttpBinResponse.class);

        System.out.println("body = " + body);
    }

    record HttpBinResponse(Map<String, String> args, Map<String, String> headers, String origin, String url) {
    }
}
