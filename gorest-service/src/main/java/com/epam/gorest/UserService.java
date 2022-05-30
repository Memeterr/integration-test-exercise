package com.epam.gorest;

import com.epam.gorest.client.UserWebClient;
import com.epam.gorest.domain.GoRestResponse;
import com.epam.gorest.domain.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserWebClient userWebClient;
    private final Mapper mapper;

    public UserService(UserWebClient userWebClient, Mapper mapper) {
        this.userWebClient = userWebClient;
        this.mapper = mapper;
    }

    public User createUser() {
        GoRestResponse response = userWebClient.createUser(randomEmail()).orElseGet(GoRestResponse::new);
        return mapper.toUser(response);
    }

    private String randomEmail() {
        return UUID.randomUUID().toString().substring(0,8) + "@random.com";
    }
}
