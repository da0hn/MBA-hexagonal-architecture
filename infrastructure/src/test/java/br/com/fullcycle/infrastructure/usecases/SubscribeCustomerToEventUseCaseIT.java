package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.domain.ticket.TicketRepository;
import br.com.fullcycle.domain.ticket.TicketStatus;
import br.com.fullcycle.infrastructure.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

class SubscribeCustomerToEventUseCaseIT extends IntegrationTest {

  @Autowired
  private SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PartnerRepository partnerRepository;

  @Autowired
  private TicketRepository ticketRepository;

  private Event createEvent(
    final String name,
    final LocalDate date,
    final int totalSpots,
    final Partner partner
  ) {
    return this.eventRepository.create(Event.newEvent(name, date.toString(), totalSpots, partner));
  }

  private Customer createCustomer(final String name, final String cpf, final String email) {
    return this.customerRepository.create(Customer.newCustomer(name, cpf, email));
  }

  private Partner createPartner(final String name, final String cnpj, final String email) {
    return this.partnerRepository.create(Partner.newPartner(name, cnpj, email));
  }

  @AfterEach
  void tearDown() {
    this.eventRepository.deleteAll();
    this.customerRepository.deleteAll();
    this.ticketRepository.deleteAll();
    this.partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve comprar um ticket de um evento")
  void testReserveTicket() {

    final var customer = this.createCustomer("John Doe", "123.456.789-00", "john.doe@gmai.com");
    final var partner = this.createPartner("Partner", "12.345.678/9012-34", "partner@gmail.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 1, partner);

    final var eventId = event.eventId().asString();
    final var customerId = customer.customerId().asString();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var output = this.subscribeCustomerToEventUseCase.execute(input);

    Assertions.assertThat(output.eventId()).isNotNull();
    Assertions.assertThat(output.eventTicketId()).isNotNull();
    Assertions.assertThat(output.reservedAt()).isNotNull();
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um evento que não existe")
  void testReserveTicketWithoutEvent() {
    final var expectedMessage = "Event not found";

    final var customer = this.createCustomer("John Doe", "123.456.789-00", "john.doe@gmai.com");

    final var customerId = customer.customerId().asString();
    final var eventId = UUID.randomUUID().toString();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um cliente que não existe")
  void testReserveTicketWithoutCustomer() {

    final var expectedMessage = "Customer not found";
    final var partner = this.createPartner("Partner", "12.345.678/9012-34", "partner@gmail.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 1, partner);
    final var eventId = event.eventId().asString();
    final var customerId = UUID.randomUUID().toString();

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o cliente já comprou")
  void testReserveTicketMoreThanOnce() {
    final var expectedMessage = "Email already registered";
    final var customer = this.createCustomer("John Doe", "123.456.789-00", "john.doe@gmai.com");
    final var partner = Partner.newPartner("Partner", "12.345.678/9012-34", "partner@gmail.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 5, partner);

    final var customerId = customer.customerId().asString();
    final var eventId = event.eventId().asString();

    event.reserveTicket(customer.customerId());
    this.eventRepository.update(event);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o evento esgotado")
  void testReserveTicketWithoutSlots() {

    final var expectedMessage = "Event sold out";

    final var otherCustomer = this.createCustomer("John Doe", "123.456.789-00", "john.doe@gmai.com");
    final var customer = this.createCustomer("John Doe", "123.456.789-00", "john.doe@gmai.com");
    final var partner = Partner.newPartner("Partner", "12.345.678/9012-34", "partner@gmail.com");
    final var event = this.createEvent("Event name", LocalDate.now().plusWeeks(2), 1, partner);

    final var customerId = customer.customerId().asString();
    final var eventId = event.eventId().asString();

    event.reserveTicket(otherCustomer.customerId());
    this.eventRepository.update(event);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    Assertions.assertThatThrownBy(() -> this.subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

}
