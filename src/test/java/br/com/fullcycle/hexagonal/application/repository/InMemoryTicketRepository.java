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
  public Optional<Ticket> ticketOfId(final TicketId ticketId) {
    return Optional.ofNullable(this.tickets.getOrDefault(ticketId.asString(), null));
  }

  @Override
  public Ticket create(final Ticket ticket) {
    this.tickets.put(ticket.eventId().asString(), ticket);
    return ticket;
  }

  @Override
  public Ticket update(final Ticket ticket) {
    this.tickets.put(ticket.eventId().asString(), ticket);
    return ticket;
  }

  @Override
  public void deleteAll() {
    this.tickets.clear();
  }

}
