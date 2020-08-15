package com.test.londonusers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/**
 * Class defining User object both being consumed and returning.
 */
@Value
public class User {

  @JsonProperty("id")
  private int id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("ip_address")
  private String ipAddress;

  @JsonProperty("latitude")
  private double latitude;

  @JsonProperty("longitude")
  private double longitude;
}