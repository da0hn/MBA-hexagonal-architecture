package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.Entities;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.ticket.TicketId;

public class EventTicket {

  private final TicketId ticketId;

  private final EventId eventId;

  private final CustomerId customerId;

  private final int ordering;

  public EventTicket(final TicketId ticketId, final EventId eventId, final CustomerId customerId, final Integer ordering) {
    this.ticketId = Entities.requireNonNull(ticketId, "Ticket id cannot be null");
    this.eventId = Entities.requireNonNull(eventId, "Event id cannot be null");
    this.customerId = Entities.requireNonNull(customerId, "Customer id cannot be null");
    this.ordering = Entities.requireNonNull(ordering, "Ordering cannot be null");
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


}
