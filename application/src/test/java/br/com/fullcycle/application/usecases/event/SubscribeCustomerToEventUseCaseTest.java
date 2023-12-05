package br.com.fullcycle.application.usecases.event;

import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.application.repository.InMemoryEventRepository;
import br.com.fullcycle.application.repository.InMemoryTicketRepository;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.ticket.TicketStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubscribeCustomerToEventUseCaseTest {

  @Test
  @DisplayName("Deve comprar um ticket de um evento")
  void testReserveTicket() {

    final var eventRepository = new InMemoryEventRepository();
    final var customerRepository = new InMemoryCustomerRepository();
    final var ticketRepository = new InMemoryTicketRepository();

    final var customer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
    final var partner = Partner.newPartner("Partner 1", "12.345.678/0001-91", "partner@gmail.com");
    final var event = Event.newEvent(
      "Event 1",
      "2023-12-04",
      10,
      partner
    );

    eventRepository.create(event);
    customerRepository.create(customer);

    final var input = new SubscribeCustomerToEventUseCase.Input(event.eventId().asString(), customer.customerId().asString());

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(
      eventRepository,
      customerRepository,
      ticketRepository
    );

    final var output = subscribeCustomerToEventUseCase.execute(input);

    Assertions.assertThat(output.eventId()).isNotNull();
    Assertions.assertThat(output.reservedAt()).isNotNull();
    Assertions.assertThat(output.ticketStatus()).isEqualTo(TicketStatus.PENDING.name());
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um evento que não existe")
  void testReserveTicketWithoutEvent() {

    final var eventRepository = new InMemoryEventRepository();
    final var customerRepository = new InMemoryCustomerRepository();
    final var ticketRepository = new InMemoryTicketRepository();

    final var expectedMessage = "Event not found";

    final var customer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
    customerRepository.create(customer);

    final var customerId = customer.customerId().asString();
    final var eventId = EventId.unique().asString();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(
      eventRepository,
      customerRepository,
      ticketRepository
    );

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um cliente que não existe")
  void testReserveTicketWithoutCustomer() {

    final var eventRepository = new InMemoryEventRepository();
    final var customerRepository = new InMemoryCustomerRepository();
    final var ticketRepository = new InMemoryTicketRepository();
    final var expectedMessage = "Customer not found";
    final var partner = Partner.newPartner("Partner 1", "12.345.678/0001-91", "partner@gmail.com");
    final var event = Event.newEvent(
      "Event 1",
      "2023-12-04",
      10,
      partner
    );
    final var customerId = CustomerId.unique().asString();

    eventRepository.create(event);

    final var input = new SubscribeCustomerToEventUseCase.Input(event.eventId().asString(), customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(
      eventRepository,
      customerRepository,
      ticketRepository
    );

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o cliente já comprou")
  void testReserveTicketMoreThanOnce() {

    final var eventRepository = new InMemoryEventRepository();
    final var customerRepository = new InMemoryCustomerRepository();
    final var ticketRepository = new InMemoryTicketRepository();

    final var expectedMessage = "Email already registered";
    final var customer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
    final var partner = Partner.newPartner("Partner 1", "12.345.678/0001-91", "partner@gmail.com");
    final var event = Event.newEvent(
      "Event 1",
      "2023-12-04",
      10,
      partner
    );

    final var ticket = event.reserveTicket(customer.customerId());

    ticketRepository.create(ticket);
    eventRepository.create(event);
    customerRepository.create(customer);

    final var input = new SubscribeCustomerToEventUseCase.Input(event.eventId().asString(), customer.customerId().asString());

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(
      eventRepository,
      customerRepository,
      ticketRepository
    );

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o evento esgotado")
  void testReserveTicketWithoutSlots() {

    final var eventRepository = new InMemoryEventRepository();
    final var customerRepository = new InMemoryCustomerRepository();
    final var ticketRepository = new InMemoryTicketRepository();

    final var expectedMessage = "Event sold out";
    final var customer = Customer.newCustomer("John Doe", "123.456.789-01", "john.doe@gmail.com");
    final var otherCustomer = Customer.newCustomer("John Doe 2", "123.456.789-02", "john.doe2@gmail.com");
    final var partner = Partner.newPartner("Partner 1", "12.345.678/0001-91", "partner@gmail.com");
    final var event = Event.newEvent(
      "Event 1",
      "2023-12-04",
      1,
      partner
    );

    final var ticket = event.reserveTicket(otherCustomer.customerId());

    ticketRepository.create(ticket);
    eventRepository.create(event);
    customerRepository.create(customer);

    final var input = new SubscribeCustomerToEventUseCase.Input(event.eventId().asString(), customer.customerId().asString());

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(
      eventRepository,
      customerRepository,
      ticketRepository
    );

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

}
