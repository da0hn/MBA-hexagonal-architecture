package br.com.fullcycle.hexagonal.application.domain.ticket;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.Entities;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketStatus;

import java.time.Instant;

public class Ticket {

  private final TicketId ticketId;

  private final CustomerId customerId;

  private final EventId eventId;

  private final TicketStatus status;

  private final Instant paidAt;

  private final Instant reservedAt;

  public Ticket(
    final TicketId ticketId,
    final CustomerId customerId,
    final EventId eventId,
    final TicketStatus status,
    final Instant paidAt,
    final Instant reservedAt
  ) {
    this.ticketId = Entities.requireNonNull(ticketId, "Ticket id must not be null");
    this.customerId = Entities.requireNonNull(customerId, "Ticket customer id must not be null");
    this.eventId = Entities.requireNonNull(eventId, "Ticket event id must not be null");
    this.status = Entities.requireNonNull(status, "Ticket status must not be null");
    this.reservedAt = Entities.requireNonNull(reservedAt, "Ticket reserved at must not be null");
    this.paidAt = paidAt;
  }

  public static Ticket newTicket(
    final CustomerId customerId,
    final EventId eventId
  ) {
    return new Ticket(
      TicketId.unique(),
      customerId,
      eventId,
      TicketStatus.PENDING,
      null,
      Instant.now()
    );
  }

  public TicketId ticketId() {
    return this.ticketId;
  }

  public CustomerId customerId() {
    return this.customerId;
  }

  public EventId eventId() {
    return this.eventId;
  }

  public TicketStatus status() {
    return this.status;
  }

  public Instant paidAt() {
    return this.paidAt;
  }

  public Instant reservedAt() {
    return this.reservedAt;
  }

}
