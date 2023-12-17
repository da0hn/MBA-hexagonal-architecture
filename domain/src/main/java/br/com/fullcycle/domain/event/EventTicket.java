package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.Entities;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.ticket.TicketId;

public class EventTicket {

  private final EventTicketId eventTicketId;

  private final EventId eventId;

  private final CustomerId customerId;

  private final int ordering;

  private TicketId ticketId;

  public EventTicket(
    final EventTicketId eventTicketId,
    final TicketId ticketId,
    final EventId eventId,
    final CustomerId customerId,
    final Integer ordering
  ) {
    this.ticketId = ticketId;
    this.eventTicketId = Entities.requireNonNull(eventTicketId, "EventTicket id cannot be null");
    this.eventId = Entities.requireNonNull(eventId, "Event id cannot be null");
    this.customerId = Entities.requireNonNull(customerId, "Customer id cannot be null");
    this.ordering = Entities.requireNonNull(ordering, "Ordering cannot be null");
  }

  public static EventTicket newTicket(final EventId eventId, final CustomerId customerId, final Integer ordering) {
    return new EventTicket(EventTicketId.unique(), null, eventId, customerId, ordering);
  }

  public EventTicketId eventTicketId() {
    return this.eventTicketId;
  }

  public TicketId ticketId() {
    return this.ticketId;
  }

  public EventId eventId() {
    return this.eventId;
  }

  public CustomerId customerId() {
    return this.customerId;
  }

  public int ordering() {
    return this.ordering;
  }

  public EventTicket associateTicket(final TicketId ticketId) {
    this.ticketId = ticketId;
    return this;
  }

}
