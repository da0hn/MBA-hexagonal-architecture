package br.com.fullcycle.domain.ticket;


import java.util.Optional;

public interface TicketRepository {

  Optional<Ticket> ticketOfId(TicketId ticketId);

  Ticket create(Ticket ticket);

  Ticket update(Ticket ticket);

  void deleteAll();

}
