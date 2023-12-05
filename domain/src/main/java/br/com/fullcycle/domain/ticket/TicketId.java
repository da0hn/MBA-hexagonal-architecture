package br.com.fullcycle.domain.ticket;


import br.com.fullcycle.domain.exceptions.ValidationException;

import java.util.UUID;

public record TicketId(UUID value) {

  public static TicketId unique() {
    return new TicketId(UUID.randomUUID());
  }

  public static TicketId with(final String value) {
    try {
      return new TicketId(UUID.fromString(value));
    }
    catch (final Exception e) {
      throw new ValidationException("Invalid value for ticket id");
    }
  }

  public String asString() {
    return this.value.toString();
  }

}
