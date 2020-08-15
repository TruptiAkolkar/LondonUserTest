package com.test.londonusers.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.test.londonusers.model.User;

public class UserTest {

  @Test
  public void testConstructor() {
    User user = new User(
        1,
        "John",
        "Smith",
        "email@domain.com",
        "0.0.0.0",
        123.456,
        654.321);

    assertThat("id has been set", user.getId(), is(1));
    assertThat("firstName has been set", user.getFirstName(), is("John"));
    assertThat("lastName has been set", user.getLastName(), is("Smith"));
    assertThat("email has been set", user.getEmail(), is("email@domain.com"));
    assertThat("ipAddress has been set", user.getIpAddress(), is("0.0.0.0"));
    assertThat("latitude has been set", user.getLatitude(), is(123.456));
    assertThat("longitude has been set", user.getLongitude(), is(654.321));
  }
}