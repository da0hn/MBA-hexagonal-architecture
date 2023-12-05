package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.util.UUID;

public record CustomerId(UUID value) {

  public static CustomerId unique() {
    return new CustomerId(UUID.randomUUID());
  }

  public static CustomerId with(final String value) {
    try {
      return new CustomerId(UUID.fromString(value));
    }
    catch (final Exception e) {
      throw new ValidationException("Invalid value for customer id");
    }
  }

  public String asString() {
    return this.value.toString();
  }

}
