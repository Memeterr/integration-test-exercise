package com.epam.gorest;

import com.epam.gorest.domain.GoRestResponse;
import com.epam.gorest.domain.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public User toUser(GoRestResponse goRestResponse) {
        return new User(
                goRestResponse.getId(),
                goRestResponse.getName(),
                goRestResponse.getEmail(),
                goRestResponse.getGender(),
                goRestResponse.getStatus()
        );
    }

}
