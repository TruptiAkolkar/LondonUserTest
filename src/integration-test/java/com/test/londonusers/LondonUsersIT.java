package com.test.londonusers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.test.londonusers.LondonUsersApplication;
import com.test.londonusers.controller.LondonUsersController;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LondonUsersApplication.class})
@ActiveProfiles("IT")
@WebAppConfiguration	
public class LondonUsersIT {

  @Rule
  public WireMockRule wireMockRuleCV = new WireMockRule(options()
      .usingFilesUnderDirectory("src/integration-test/resources/wiremock")
      .port(8080));

  @Autowired
  private LondonUsersController londonUsersController;

  @Before
  public void setup() {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(londonUsersController).build();
    RestAssuredMockMvc.mockMvc(mockMvc);
  }
  @Test
  public void givenBackendReturnsEmptyList_FullService_ReturnsEmptyList() {

    stubFor(get(urlEqualTo("/city/London/users"))
        .willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[]")
        ));

    stubFor(get(urlEqualTo("/users"))
        .willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[]")
        ));

    RestAssuredMockMvc
        .when().get("/v1/london-users")
        .then().expect(jsonPath("$", hasSize(0)))
        .and().statusCode(200);
  }

  @Test
  public void givenBackendWithAllUsersMocked_FullService_ReturnsAllUsersFromLondonAndAroundAsServiceDoes() {

    stubFor(get(urlEqualTo("/city/London/users"))
        .willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("londonUsers.response.json")
        ));

    stubFor(get(urlEqualTo("/users"))
        .willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("allUsers.response.json")
        ));

    RestAssuredMockMvc
        .when().get("/v1/london-users")
        .then().expect(jsonPath("$", hasSize(9)))
        .and().statusCode(200);
  }
}