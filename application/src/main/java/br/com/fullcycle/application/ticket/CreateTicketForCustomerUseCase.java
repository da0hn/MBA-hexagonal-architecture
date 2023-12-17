package br.com.fullcycle.application.ticket;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.ticket.Ticket;
import br.com.fullcycle.domain.ticket.TicketRepository;

public class CreateTicketForCustomerUseCase
  extends UseCase<CreateTicketForCustomerUseCase.Input, CreateTicketForCustomerUseCase.Output> {

  private final TicketRepository ticketRepository;

  public CreateTicketForCustomerUseCase(final TicketRepository ticketRepository) { this.ticketRepository = ticketRepository; }

  @Override
  public Output execute(final Input input) {

    final var ticket = Ticket.newTicket(
      EventTicketId.with(input.eventTicketId()),
      CustomerId.with(input.customerId()),
      EventId.with(input.eventId())
    );

    this.ticketRepository.create(ticket);

    return new Output(ticket.ticketId().asString());
  }

  public record Input(String eventTicketId, String eventId, String customerId) { }

  public record Output(String ticketId) { }

}
