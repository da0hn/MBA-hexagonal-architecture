package br.com.fullcycle.hexagonal.application.domain.person;

import org.springframework.util.Assert;

public record Email(String value) {

  public Email {
    Assert.notNull(value, "Email cannot be null");
    Assert.state(!value.isBlank(), "Email cannot be empty");
    Assert.state(value.matches("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$"), "Invalid value for email");
  }

}
