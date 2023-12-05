package br.com.fullcycle.hexagonal.application.domain.person;

import org.springframework.util.Assert;

public record Name(String value) {

  public Name {
    Assert.notNull(value, "Name value cannot be null");
    Assert.state(!value.isBlank(), "Name value cannot be blank");
  }

}
