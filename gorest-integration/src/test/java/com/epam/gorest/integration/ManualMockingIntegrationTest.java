package com.epam.gorest.integration;

import com.epam.gorest.GorestApplication;
import com.epam.gorest.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("integration")
@SpringBootTest(classes = GorestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManualMockingIntegrationTest {

    @Value("classpath:/response/mock_user.json")
    private Resource mockUserJsonFile;

    @Autowired
    private FileReader fileReader;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGoRestUserShouldReturnMockedUser() {
        // GIVEN

        // WHEN
        UserResponse actual = testRestTemplate.postForObject("http://localhost:" + port + "/api/user", null, UserResponse.class);

        // THEN
        UserResponse expected = fileReader.read(mockUserJsonFile, UserResponse.class);
        assertEquals(expected, actual);
    }

}
