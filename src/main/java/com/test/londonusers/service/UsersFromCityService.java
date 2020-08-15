package com.test.londonusers.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.test.londonusers.model.User;

/**
 * Service to return all users from a city.
 */
@Service
public class UsersFromCityService {

  /**
   * Base url for backend call.
   */
  private final String backendUrl;

  /**
   * Logger for this service.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersWithinRadiusService.class);

  /**
   * Define restTemplate.
   */
  private RestTemplate restTemplate;

  /**
   * Constructor for the Service with restTemplate bean.
   * @param restTemplate used to make the backend request.
   */
  @Autowired
  public UsersFromCityService(RestTemplate restTemplate, final @Value("${backend_service_url}") String url) {
    this.restTemplate = restTemplate;
    this.backendUrl = url;
  }

  /**
   * Get all users in a list from a given city.
   *
   * @param city - String name of city to allow the methid to be reused for future requirements.
   * @return - List of users from given city.
   */
  public List<User> getUsersFromCity(final String city) {

    final String requestUrl = backendUrl + "city/" + city + "/users";

    LOGGER.info("Request all users listed as from London.");
    final ResponseEntity<User[]> responseEntity =
        restTemplate.exchange(requestUrl, HttpMethod.GET, null, User[].class);

    final User[] users = responseEntity.getBody();

    return users != null ? Arrays.asList(users) : new ArrayList<>();
  }
}