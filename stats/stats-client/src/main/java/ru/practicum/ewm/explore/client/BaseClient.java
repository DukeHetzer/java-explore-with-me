package ru.practicum.ewm.explore.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class BaseClient {
    protected final RestTemplate rest;

    protected <T> ResponseEntity<Object> post(T body) {
        return makeAndSendRequest(body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, new HttpHeaders());

        ResponseEntity<Object> statisticServerResponse;
        try {
            statisticServerResponse = rest.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statisticServerResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}