package com.test.londonusers.controller;

import com.test.londonusers.model.User;
import com.test.londonusers.service.UsersInOrAroundLondonService;
import com.test.londonusers.service.UsersWithinRadiusService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller defining the REST endpoint of the application.
 */
@RestController
public class LondonUsersController {

  /**
   * Logger for this service.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(UsersWithinRadiusService.class);

  /**
   * UsersInOrAroundCityService init.
   */
  private final UsersInOrAroundLondonService usersInOrAroundLondonService;

  /**
   * Constructor for controller autowiring service.
   *
   * @param usersInOrAroundLondonService - service to get users in our around city.
   */
  @Autowired
  public LondonUsersController(UsersInOrAroundLondonService usersInOrAroundLondonService) {
    this.usersInOrAroundLondonService = usersInOrAroundLondonService;
  }

  @GetMapping(value = "/v1/london-users", produces = {"application/json"})
  public ResponseEntity<List<User>> getLondonUsers() {

    LOGGER.info("Received request, call service to get corresponding users");

    List<User> londonUsers = usersInOrAroundLondonService.getUsersInOrAroundLondonService();

    return ResponseEntity.ok().body(londonUsers);
  }
}