package com.epam.gorest.integration;

import com.epam.gorest.GorestApplication;
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
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

@AutoConfigureMockMvc
@ActiveProfiles("wiremock")
@SpringBootTest(classes = GorestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:/response/mock_mvc_response.json")
    private Resource mockUserJsonFile;

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
    public void testUserShouldReturnMockUserWhenUsingWireMock() throws Exception {
        // GIVEN
        // FIXME
        stubFor(WireMock.post(urlMatching("/public/v2/users"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("gorest_mock_response.json")));

        System.out.println(new String(fileReader.readFileToBytes(mockUserJsonFile)));

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(fileReader.readFileToBytes(mockUserJsonFile)));

        // THEN
    }

}
