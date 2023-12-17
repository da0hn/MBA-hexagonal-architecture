package br.com.fullcycle.domain.event;


import br.com.fullcycle.domain.exceptions.ValidationException;

import java.util.UUID;

public record EventTicketId(UUID value) {

  public static EventTicketId unique() {
    return new EventTicketId(UUID.randomUUID());
  }

  public static EventTicketId with(final String value) {
    try {
      return new EventTicketId(UUID.fromString(value));
    }
    catch (final Exception e) {
      throw new ValidationException("Invalid value for event id");
    }
  }

  public String asString() {
    return this.value.toString();
  }

}
