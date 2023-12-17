package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;

import java.time.Instant;
import java.util.UUID;

public record EventTicketReserved(String domainEventId, String type, String eventTicketId, String eventId, String customerId, Instant occurredOn)
  implements DomainEvent {

  public EventTicketReserved(final EventTicketId eventTicketId, final EventId eventId, final CustomerId customerId) {
    this(
      UUID.randomUUID().toString(),
      "event-ticket-reserved",
      eventTicketId.asString(),
      eventId.asString(),
      customerId.asString(),
      Instant.now()
    );
  }

}
