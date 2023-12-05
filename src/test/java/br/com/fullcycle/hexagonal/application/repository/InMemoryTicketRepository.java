package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryTicketRepository implements TicketRepository {

  private final Map<String, Ticket> tickets;

  public InMemoryTicketRepository() {
    this.tickets = new HashMap<>();
  }

  @Override
  public Optional<Ticket> ticketOfId(final TicketId eventId) {
    return Optional.ofNullable(this.tickets.getOrDefault(eventId.asString(), null));
  }

  @Override
  public Ticket create(final Ticket event) {
    this.tickets.put(event.eventId().asString(), event);
    return event;
  }

  @Override
  public Ticket update(final Ticket event) {
    this.tickets.put(event.eventId().asString(), event);
    return event;
  }

}
