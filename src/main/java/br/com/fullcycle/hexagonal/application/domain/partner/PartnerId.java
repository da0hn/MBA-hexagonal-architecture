package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.util.UUID;

public record PartnerId(UUID value) {

  public static PartnerId unique() {
    return new PartnerId(UUID.randomUUID());
  }

  public static PartnerId with(final String value) {
    try {
      return new PartnerId(UUID.fromString(value));
    }
    catch (final Exception e) {
      throw new ValidationException("Invalid value for customer id");
    }
  }

  public String asString() {
    return this.value.toString();
  }

}
