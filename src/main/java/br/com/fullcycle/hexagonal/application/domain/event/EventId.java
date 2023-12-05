package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.util.UUID;

public record EventId(UUID value) {

  public static EventId unique() {
    return new EventId(UUID.randomUUID());
  }

  public static EventId with(final String value) {
    try {
      return new EventId(UUID.fromString(value));
    }
    catch (final Exception e) {
      throw new ValidationException("Invalid value for event id");
    }
  }

  public String asString() {
    return this.value.toString();
  }

}
