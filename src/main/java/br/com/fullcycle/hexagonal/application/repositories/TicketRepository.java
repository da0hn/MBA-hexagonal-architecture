package br.com.fullcycle.hexagonal.application.repositories;

import br.com.fullcycle.hexagonal.application.domain.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.ticket.TicketId;

import java.util.Optional;

public interface TicketRepository {

  Optional<Ticket> ticketOfId(TicketId eventId);

  Ticket create(Ticket event);

  Ticket update(Ticket event);

}
