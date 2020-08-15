package com.test.londonusers.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.lucene.util.SloppyMath;
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
public class UsersWithinRadiusService {

  /**
   * Logger for this service.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersWithinRadiusService.class);

  /**
   * Base url for backend call.
   */
  private final String backendUrl;

  /**
   * Define restTemplate.
   */
  private RestTemplate restTemplate;

  /**
   * Constructor for the service with restTemplate bean.
   * @param restTemplate used to make the backend request.
   */
  @Autowired
  public UsersWithinRadiusService(RestTemplate restTemplate, final @Value("${backend_service_url}") String url) {
    this.restTemplate = restTemplate;
    this.backendUrl = url;
  }

  /**
   * Method to get all users within a radius of a given longitude, latitude point.
   *
   * @param latitudeOfPoint - latitude of center point to get users from.
   * @param longitudeOfPoint - longitude of center point to get users from.
   * @param radius - radius in metres around the point to get users from.
   * @return list of users within radius of long lat point.
   */
  public List<User> getUsersWithinRadius(final double latitudeOfPoint, final double longitudeOfPoint, final double radius) {

    final String requestUrl = backendUrl + "users";

    LOGGER.info("Making request to get all users.");
    final ResponseEntity<User[]> responseEntity =
        restTemplate.exchange(requestUrl, HttpMethod.GET, null, User[].class);

    User[] users = responseEntity.getBody();

    List<User> usersWithinRadius = users != null ? Arrays.asList(users) : new ArrayList<>();

    LOGGER.info("Filtering results to only users within 50 miles of London.");
    return usersWithinRadius.stream()
        .filter(user -> SloppyMath.haversinMeters(latitudeOfPoint, longitudeOfPoint, user.getLatitude(), user.getLongitude())
            <= radius)
        .collect(Collectors.toList());
  }
}