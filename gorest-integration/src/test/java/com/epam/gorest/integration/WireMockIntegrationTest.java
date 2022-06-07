package com.epam.gorest.integration;

import com.epam.gorest.GorestApplication;
import com.epam.gorest.UserResponse;
import com.epam.gorest.domain.User;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@ActiveProfiles("wiremock")
@SpringBootTest(classes = GorestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WireMockIntegrationTest {

    @Value("classpath:/response/mock_user.json")
    private Resource mockJsonFile;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FileReader fileReader;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void init() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8089)
                .withRootDirectory("src/test/resources/wiremock"));

        WireMock.configureFor(8089);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testBitcoinPricesShouldReturnMockPricesWhenUsingWireMock() throws IOException {
        // GIVEN
        stubFor(WireMock.post(urlMatching("/public/v2/users"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("gorest_mock_response.json")));

        // WHEN
        UserResponse actualResponse = this.testRestTemplate.getForObject("http://localhost:" + port + "/api/user", UserResponse.class);

        // THEN
        UserResponse expected = new UserResponse(fileReader.read(mockJsonFile, User.class));
        assertEquals(expected, actualResponse);
    }

}
