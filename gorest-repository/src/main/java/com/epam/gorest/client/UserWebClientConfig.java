package com.epam.gorest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserWebClientConfig {

    @Value("${gorest.baseUrl}")
    private String baseUrl;

    @Bean
    public UserWebClient goRestClient() {
        return new UserWebClient(baseUrl);
    }

}
