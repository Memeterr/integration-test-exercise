package com.epam.gorest.integration;

import com.epam.gorest.client.UserClient;
import com.epam.gorest.domain.GoRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Primary
@Profile("integration")
public class UserWebClientMock implements UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWebClientMock.class);

    @Value("classpath:gorest_mock_response.json")
    private Resource goRestMockResponse;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<GoRestResponse> createUser(String email) {
        GoRestResponse goRestResponse = null;

        try {
            goRestResponse = objectMapper.readValue(goRestMockResponse.getInputStream(), GoRestResponse.class);
        } catch (IOException exception) {
            LOGGER.error("Failed to read mock response from file.");
        }
System.out.println(goRestResponse);
        return Optional.ofNullable(goRestResponse);
    }

}
