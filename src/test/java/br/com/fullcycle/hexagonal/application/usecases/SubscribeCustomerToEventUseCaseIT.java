package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.TicketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

class SubscribeCustomerToEventUseCaseIT extends IntegrationTest {

  @Autowired
  private SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TicketRepository ticketRepository;

  private Event createEvent(final String eventName, final LocalDate date, final int totalSpots, final HashSet<Ticket> tickets) {
    final var event = new Event();
    event.setName(eventName);
    event.setDate(date);
    event.setTotalSpots(totalSpots);
    event.setTickets(tickets);
    return this.eventRepository.save(event);
  }

  private Customer createCustomer(final String name, final String cpf, final String email) {
    final var customer = new Customer();
    customer.setName(name);
    customer.setCpf(cpf);
    customer.setEmail(email);
    return this.customerRepository.save(customer);
  }

  @AfterEach
  void tearDown() {
    this.eventRepository.deleteAll();
    this.customerRepository.deleteAll();
    this.ticketRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve comprar um ticket de um evento")
  void testReserveTicket() {

    final var customer = this.createCustomer("John Doe", "12345678900", "john.doe@gmai.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 1, new HashSet<>());

    final var eventId = event.getId();
    final var customerId = customer.getId();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var output = this.subscribeCustomerToEventUseCase.execute(input);

    Assertions.assertThat(output.eventId()).isNotNull();
    Assertions.assertThat(output.reservedAt()).isNotNull();
    Assertions.assertThat(output.ticketStatus()).isEqualTo(TicketStatus.PENDING.name());
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um evento que não existe")
  void testReserveTicketWithoutEvent() {
    final var expectedMessage = "Event not found";

    final var customer = this.createCustomer("John Doe", "12345678900", "john.doe@gmai.com");

    final var customerId = customer.getId();
    final var eventId = UUID.randomUUID().getMostSignificantBits();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um cliente que não existe")
  void testReserveTicketWithoutCustomer() {

    final var expectedMessage = "Customer not found";
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 1, new HashSet<>());
    final var eventId = event.getId();
    final var customerId = UUID.randomUUID().getMostSignificantBits();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o cliente já comprou")
  void testReserveTicketMoreThanOnce() {
    final var expectedMessage = "Email already registered";
    final var customer = this.createCustomer("John Doe", "12345678900", "john.doe@gmai.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 5, new HashSet<>());

    final var customerId = customer.getId();
    final var eventId = event.getId();

    final var ticket = new Ticket();
    ticket.setCustomer(customer);
    ticket.setEvent(event);
    ticket.setReservedAt(Instant.now());
    ticket.setStatus(TicketStatus.PENDING);

    this.ticketRepository.save(ticket);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o evento esgotado")
  void testReserveTicketWithoutSlots() {

    final var expectedMessage = "Event sold out";

    final var otherCustomer = this.createCustomer("John Doe", "12345678900", "john.doe@gmai.com");
    final var customer = this.createCustomer("John Doe", "12345678900", "john.doe@gmai.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 1, new HashSet<>());

    final var customerId = customer.getId();
    final var eventId = event.getId();

    final var ticket = new Ticket();
    ticket.setCustomer(otherCustomer);
    ticket.setEvent(event);
    ticket.setReservedAt(Instant.now());
    ticket.setStatus(TicketStatus.PENDING);

    event.getTickets().add(ticket);
    this.ticketRepository.save(ticket);
    this.eventRepository.save(event);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

}
