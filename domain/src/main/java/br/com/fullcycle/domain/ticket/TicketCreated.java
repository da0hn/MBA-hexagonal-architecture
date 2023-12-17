package br.com.fullcycle.domain.ticket;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicketId;

import java.time.Instant;
import java.util.UUID;

public record TicketCreated(
  String domainEventId,
  String type,
  String eventTicketId,
  String ticketId,
  String eventId,
  String customerId,
  Instant occurredOn
)
  implements DomainEvent {

  public TicketCreated(
    final EventTicketId eventTicketId,
    final TicketId ticketId,
    final EventId eventId,
    final CustomerId customerId
  ) {
    this(
      UUID.randomUUID().toString(),
      "ticket-created",
      eventTicketId.asString(),
      ticketId.asString(),
      eventId.asString(),
      customerId.asString(),
      Instant.now()
    );
  }

}
