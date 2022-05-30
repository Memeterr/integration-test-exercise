package com.epam.gorest.client;

import com.epam.gorest.domain.GoRestResponse;

import java.util.Optional;

public interface UserClient {

    Optional<GoRestResponse> createUser(String email);

}
