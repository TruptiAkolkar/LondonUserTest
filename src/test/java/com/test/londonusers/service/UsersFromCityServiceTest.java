package com.test.londonusers.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
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
import com.test.londonusers.service.UsersFromCityService;

public class UsersFromCityServiceTest {

  private RestTemplate restTemplate;
  private UsersFromCityService usersFromCityService;
  private String backendUrl;

  @Before
  public void setUp() {
    restTemplate = mock(RestTemplate .class);
    String url = "http://bpdts-test-app.herokuapp.com/";
    usersFromCityService = new UsersFromCityService(restTemplate, url);

    backendUrl = url + "city/London/users";
  }

  @Test
  public void givenMockedRestTemplateReturnsOneUser_GetUsersFromCity_ReturnsTheSingleUser() {

    User userJohn = new User(
        1,
        "John",
        "Smith",
        "email@domain.com",
        "0.0.0.0",
        123.456,
        654.321);

    List<User> expectedUsers = new ArrayList<>();
    expectedUsers.add(userJohn);

    User[] users = new User[1];
    users[0] = userJohn;

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    List<User> actualUsers = usersFromCityService.getUsersFromCity("London");

    assertThat("when resttemplate mocked users are returened", actualUsers, is(expectedUsers));
  }

  @Test
  public void givenMockedRestTemplateReturnsMultipleUsers_GetUsersFromCity_ReturnsTheUsers() {

    User userJohn = new User(
        1,
        "John",
        "Smith",
        "email@domain.com",
        "0.0.0.0",
        123.456,
        654.321);

    List<User> expectedUsers = new ArrayList<>();
    expectedUsers.add(userJohn);
    expectedUsers.add(userJohn);
    expectedUsers.add(userJohn);

    User[] users = new User[3];
    users[0] = userJohn;
    users[1] = userJohn;
    users[2] = userJohn;

    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    List<User> actualUsers = usersFromCityService.getUsersFromCity("London");

    assertThat("when restTemplate mocked 3 users are returned", actualUsers.size(), is(3));
    assertThat("when restTemplate mocked returned users are the same", actualUsers, is(expectedUsers));
  }

  @Test
  public void givenMockedBadRequest_GetUsersFromCity_ReturnsEmptyList() {


    ResponseEntity<User[]> responseEntity = new ResponseEntity<>(new User[0], HttpStatus.BAD_REQUEST);

    when(restTemplate.exchange(backendUrl, HttpMethod.GET, null, User[].class))
        .thenReturn(responseEntity);

    List<User> actualUsers = usersFromCityService.getUsersFromCity("London");

    assertThat("Service returns empty list.", actualUsers, is(new ArrayList<>()));
  }
}