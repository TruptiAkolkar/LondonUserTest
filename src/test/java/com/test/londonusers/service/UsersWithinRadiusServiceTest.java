package com.test.londonusers.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.test.londonusers.model.User;
import com.test.londonusers.service.UsersWithinRadiusService;

public class UsersWithinRadiusServiceTest {

  private RestTemplate restTemplate;
  private UsersWithinRadiusService usersWithinRadiusService;
  private String backendUrl;

  @Before
  public void setUp() {
    restTemplate = mock(RestTemplate.class);
    String url = "http://bpdts-test-app.herokuapp.com/";
    usersWithinRadiusService = new UsersWithinRadiusService(restTemplate, url);

    backendUrl = url + "users";
  }

  @Test
  public void givenNoUsers_GetUsersWithinRadius_ReturnsEmptyList() {

    User[] users = new User[0];

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    List<User> actualUsers = usersWithinRadiusService.getUsersWithinRadius(123.456, 654.321, 80467.2);

    verify(restTemplate, times(1))
        .exchange(backendUrl, HttpMethod.GET, null, User[].class);

    assertThat("If London long lat is given and the queen is the only user, she should be returned",
        actualUsers, is(new ArrayList()));
  }

  @Test
  public void givenUserAtSameLongLatAsPoint_GetUsersWithinRadius_ReturnsTheUser() {

    User userWithinRadius = new User(
        1,
        "John",
        "Smith",
        "email@domain.com",
        "0.0.0.0",
        123.456,
        654.321);

    List<User> expectedUsers = new ArrayList<>();
    expectedUsers.add(userWithinRadius);

    User[] users = new User[1];
    users[0] = userWithinRadius;

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    List<User> actualUsers = usersWithinRadiusService.getUsersWithinRadius(123.456, 654.321, 1.0);

    verify(restTemplate, times(1))
        .exchange(backendUrl, HttpMethod.GET, null, User[].class);

    assertThat("If distance between user and point is less than radius, user is returned",
        actualUsers, is(expectedUsers));
  }

  @Test
  public void givenTheQueenReturnedAndLondonLongLatGiven_GetUsersWithinRadius_ReturnsTheQueen() {

    // User with long, lat of Buckingham Palace
    User theQueen = new User(
        1,
        "Elizabeth",
        "Windsor",
        "liz_windsor@bucks.com",
        "0.0.0.0",
        51.501401,
        -0.142159);

    List<User> expectedUsers = new ArrayList<>();
    expectedUsers.add(theQueen);

    User[] users = new User[1];
    users[0] = theQueen;

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    // Call on London Long Lat with radius of 50 miles (in metres)
    List<User> actualUsers = usersWithinRadiusService.getUsersWithinRadius(51.507222, -0.1275, 80467.2);

    verify(restTemplate, times(1))
        .exchange(backendUrl, HttpMethod.GET, null, User[].class);

    assertThat("If London long lat is given and the queen is the only user, she should be returned",
        actualUsers, is(expectedUsers));
  }

  @Test
  public void givenNewcastleUserReturnedAndLondonLongLatGiven_GetUsersWithinRadius_ReturnsEmptyList() {

    // User with long, lat of Newcastle
    User jordyUser = new User(
        2,
        "Declan",
        "Donnelly",
        "dec@antanddec.com",
        "0.0.0.0",
        54.978250,
        -1.617781);

    User[] users = new User[1];
    users[0] = jordyUser;

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    // Call on London Long Lat with radius of 50 miles (in metres)
    List<User> actualUsers = usersWithinRadiusService.getUsersWithinRadius(51.507222, -0.1275, 80467.2);

    verify(restTemplate, times(1))
        .exchange(backendUrl, HttpMethod.GET, null, User[].class);

    assertThat("If London long lat is given and a newcastle user is the only user, an empty list should be returned",
        actualUsers, is(new ArrayList()));
  }

  @Test
  public void givenMultipleUsersReturnedAndLondonLongLatGiven_GetUsersWithinRadius_ReturnsOnlyUsersInRadius() {

    // User with long, lat of Buckingham Palace
    User theQueen = new User(
        1,
        "Elizabeth",
        "Windsor",
        "liz_windsor@bucks.com",
        "0.0.0.0",
        51.501401,
        -0.142159);

    // User with long, lat of Newcastle
    User jordyUser = new User(
        2,
        "Declan",
        "Donnelly",
        "dec@antanddec.com",
        "0.0.0.0",
        54.978250,
        -1.617781);

    User[] users = new User[2];
    users[0] = theQueen;
    users[1] = jordyUser;

    List<User> expectedUsers = new ArrayList<>();
    expectedUsers.add(theQueen);

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    // Call on London Long Lat with radius of 50 miles (in metres)
    List<User> actualUsers = usersWithinRadiusService.getUsersWithinRadius(51.507222, -0.1275, 80467.2);

    verify(restTemplate, times(1))
        .exchange(backendUrl, HttpMethod.GET, null, User[].class);

    assertThat("If London long lat is given and the queen is the only user, she should be returned",
        actualUsers, is(expectedUsers));
  }
}