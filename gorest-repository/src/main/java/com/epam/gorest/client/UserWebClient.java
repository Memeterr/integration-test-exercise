package com.epam.gorest.client;

import com.epam.gorest.domain.GoRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Optional;

public class UserWebClient implements UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWebClient.class);
    private static final String GOREST_USERS_ENDPOINT = "/public/v2/users";
    private static final String TOKEN = "62c1557a5740b53ff817e0be59b7ce1d534d8a7f7d7768c48f479029efb1f6a5";
    private static final String BODY =
            """
            {
                "name":"User Created by me",
                "gender":"male",
                "email":"*email*",
                "status":"active"
            }
            """;

    private final String baseUrl;

    public UserWebClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<GoRestResponse> createUser(String email) {

        GoRestResponse response = null;
        String body = BODY.replace("*email*", email);

        try {
            URI uri = URI.create(baseUrl + GOREST_USERS_ENDPOINT);
            response = WebClient.create()
                    .post()
                    .uri(uri).headers(h -> h.setBearerAuth(TOKEN))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(GoRestResponse.class)
                    .block();
        } catch (Exception exception) {
            LOGGER.error("Error occurred: " + exception.getMessage());
        }

        return Optional.ofNullable(response);
    }



}
