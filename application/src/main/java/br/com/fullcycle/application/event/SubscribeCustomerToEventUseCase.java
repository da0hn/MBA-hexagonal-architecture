package br.com.fullcycle.application.event;

import br.com.fullcycle.application.UseCase;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.ticket.TicketRepository;

import java.time.Instant;
import java.util.Objects;

public class SubscribeCustomerToEventUseCase
  extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

  private final EventRepository eventRepository;

  private final CustomerRepository customerRepository;

  private final TicketRepository ticketRepository;

  public SubscribeCustomerToEventUseCase(
    final EventRepository eventRepository,
    final CustomerRepository customerRepository,
    final TicketRepository ticketRepository
  ) {
    this.eventRepository = Objects.requireNonNull(eventRepository);
    this.customerRepository = Objects.requireNonNull(customerRepository);
    this.ticketRepository = Objects.requireNonNull(ticketRepository);
  }

  public Output execute(final Input input) {

    final var customer = this.customerRepository.customerOfId(CustomerId.with(input.customerId()))
      .orElseThrow(() -> new ValidationException("Customer not found"));

    final var event = this.eventRepository.eventOfId(EventId.with(input.eventId()))
      .orElseThrow(() -> new ValidationException("Event not found"));

    final var ticket = event.reserveTicket(customer.customerId());

    this.eventRepository.update(event);

    return new Output(event.eventId().asString(), ticket.eventTicketId().asString(), Instant.now());

  }

  public record Input(String eventId, String customerId) { }

  public record Output(String eventId, String eventTicketId, Instant reservedAt) { }

}
