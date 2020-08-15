package com.test.londonusers.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.londonusers.model.User;

@Service
public class UsersInOrAroundLondonService {

  /**
   * String constant for London.
   */
  final private String LONDON = "London";

  /**
   * Constant for radius in meters (50 miles in metres).
   */
  final private double RADIUS = 80467.2;

  /**
   * Constant for the latitude of London.
   */
  final private double LONDON_LATITUDE = 51.507222;

  /**
   * Constant for the longitude of London.
   */
  final private double LONDON_LONGITUDE = -0.1275;

  /**
   * Logger for this service.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersWithinRadiusService.class);

  /**
   * Users from city service init.
   */
  private final UsersFromCityService usersFromCityService;

  /**
   * Users within radius service init.
   */
  private final UsersWithinRadiusService usersWithinRadiusService;

  /**
   * Controller for service autowiring called services.
   *
   * @param usersFromCityService - service to get users from london.
   * @param usersWithinRadiusService - service to get users around london.
   */
  @Autowired
  public UsersInOrAroundLondonService(UsersFromCityService usersFromCityService,
      UsersWithinRadiusService usersWithinRadiusService) {
    this.usersFromCityService = usersFromCityService;
    this.usersWithinRadiusService = usersWithinRadiusService;
  }

  /**
   * Method to get all users in London or within a 50 mile radius London.
   *
   * @return Combined list of all users in london or within 50 miles.
   */
  public List<User> getUsersInOrAroundLondonService() {

    List<User> londonUsers = usersFromCityService.getUsersFromCity(LONDON);

    List<User> usersWithinFiftyMilesOfLondon =
        usersWithinRadiusService.getUsersWithinRadius(LONDON_LATITUDE, LONDON_LONGITUDE, RADIUS);


    LOGGER.info("Combine results from both queries.");
    return Stream.of(londonUsers, usersWithinFiftyMilesOfLondon)
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }
}
