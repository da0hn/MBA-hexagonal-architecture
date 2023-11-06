package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.models.Event;
import br.com.fullcycle.hexagonal.models.Ticket;
import br.com.fullcycle.hexagonal.models.TicketStatus;
import br.com.fullcycle.hexagonal.services.CustomerService;
import br.com.fullcycle.hexagonal.services.EventService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

class SubscribeCustomerToEventUseCaseTest {

  @Test
  @DisplayName("Deve comprar um ticket de um evento")
  void testReserveTicket() {

    final var eventService = Mockito.mock(EventService.class);
    final var customerService = Mockito.mock(CustomerService.class);

    final var customerId = UUID.randomUUID().getMostSignificantBits();
    final var eventId = UUID.randomUUID().getMostSignificantBits();

    final var customer = new Customer();
    customer.setId(customerId);
    final var event = new Event();
    event.setId(eventId);
    event.setTickets(new HashSet<>());
    event.setTotalSpots(1);

    Mockito.doReturn(Optional.of(customer))
      .when(customerService)
      .findById(customerId);
    Mockito.doReturn(Optional.of(event))
      .when(eventService)
      .findById(eventId);
    Mockito.doReturn(Optional.empty())
      .when(eventService)
      .findTicketByEventIdAndCustomerId(eventId, customerId);
    Mockito.doReturn(event)
      .when(eventService)
      .save(Mockito.any());

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    final var output = subscribeCustomerToEventUseCase.execute(input);

    Assertions.assertThat(output.eventId()).isNotNull();
    Assertions.assertThat(output.reservedAt()).isNotNull();
    Assertions.assertThat(output.ticketStatus()).isEqualTo(TicketStatus.PENDING.name());
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um evento que não existe")
  void testReserveTicketWithoutEvent() {

    final var eventService = Mockito.mock(EventService.class);
    final var customerService = Mockito.mock(CustomerService.class);

    final var expectedMessage = "Event not found";
    final var customerId = UUID.randomUUID().getMostSignificantBits();
    final var eventId = UUID.randomUUID().getMostSignificantBits();

    final var customer = new Customer();
    customer.setId(customerId);

    Mockito.doReturn(Optional.of(customer))
      .when(customerService)
      .findById(customerId);
    Mockito.doReturn(Optional.empty())
      .when(eventService)
      .findById(eventId);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket de um cliente que não existe")
  void testReserveTicketWithoutCustomer() {

    final var eventService = Mockito.mock(EventService.class);
    final var customerService = Mockito.mock(CustomerService.class);

    final var customerId = UUID.randomUUID().getMostSignificantBits();
    final var eventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedMessage = "Customer not found";

    Mockito.doReturn(Optional.empty())
      .when(customerService)
      .findById(customerId);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o cliente já comprou")
  void testReserveTicketMoreThanOnce() {

    final var eventService = Mockito.mock(EventService.class);
    final var customerService = Mockito.mock(CustomerService.class);

    final var expectedMessage = "Email already registered";
    final var customerId = UUID.randomUUID().getMostSignificantBits();
    final var eventId = UUID.randomUUID().getMostSignificantBits();

    final var customer = new Customer();
    customer.setId(customerId);
    final var event = new Event();
    event.setId(eventId);
    event.setTickets(new HashSet<>());
    event.setTotalSpots(1);

    Mockito.doReturn(Optional.of(customer))
      .when(customerService)
      .findById(customerId);
    Mockito.doReturn(Optional.of(event))
      .when(eventService)
      .findById(eventId);
    Mockito.doReturn(Optional.of(new Ticket()))
      .when(eventService)
      .findTicketByEventIdAndCustomerId(eventId, customerId);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

  @Test
  @DisplayName("Não deve comprar um ticket quando o evento esgotado")
  void testReserveTicketWithoutSlots() {

    final var eventService = Mockito.mock(EventService.class);
    final var customerService = Mockito.mock(CustomerService.class);

    final var expectedMessage = "Event sold out";
    final var customerId = UUID.randomUUID().getMostSignificantBits();
    final var eventId = UUID.randomUUID().getMostSignificantBits();

    final var customer = new Customer();
    customer.setId(customerId);
    final var event = new Event();
    event.setId(eventId);
    event.setTickets(new HashSet<>());
    event.setTotalSpots(1);

    final var aTicket = new Ticket();
    aTicket.setId(UUID.randomUUID().getMostSignificantBits());
    event.getTickets().add(aTicket);

    Mockito.doReturn(Optional.of(customer))
      .when(customerService)
      .findById(customerId);
    Mockito.doReturn(Optional.of(event))
      .when(eventService)
      .findById(eventId);
    Mockito.doReturn(Optional.empty())
      .when(eventService)
      .findTicketByEventIdAndCustomerId(eventId, customerId);

    final var input = new SubscribeCustomerToEventUseCase.Input(eventId, customerId);

    final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    Assertions.assertThatThrownBy(() -> subscribeCustomerToEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage(expectedMessage);
  }

}
