package br.com.fullcycle.domain;

import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.ticket.TicketStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EventTest {

  @Test
  @DisplayName("Deve instanciar um evento")
  void test1() {
    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "Evento 1";
    final var expectedDate = "2023-12-05";
    final var expectedTotalSpots = 10;

    final var event = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

    Assertions.assertThat(event.eventId()).isNotNull();
    Assertions.assertThat(event.name().value()).isEqualTo(expectedName);
    Assertions.assertThat(event.date()).isEqualTo(expectedDate);
    Assertions.assertThat(event.totalSpots()).isEqualTo(expectedTotalSpots);
    Assertions.assertThat(event.partnerId().value()).isEqualTo(partner.partnerId().value());
    Assertions.assertThat(event.tickets()).hasSize(0);
  }

  @Test
  @DisplayName("Não deve instanciar um evento com nome inválido")
  void test2() {
    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "   ";
    final var expectedDate = "2023-12-05";
    final var expectedTotalSpots = 10;

    Assertions.assertThatThrownBy(() -> Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Name value cannot be blank");
  }

  @Test
  @DisplayName("Não deve instanciar um evento com data inválida")
  void test3() {
    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "Evento 1";
    final var expectedTotalSpots = 10;

    Assertions.assertThatThrownBy(() -> Event.newEvent(expectedName, null, expectedTotalSpots, partner))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Date must not be null");
  }

  @Test
  @DisplayName("Não deve instanciar um evento com total de spots inválido")
  void test4() {
    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "Evento 1";
    final var expectedDate = "2023-10-05";

    Assertions.assertThatThrownBy(() -> Event.newEvent(expectedName, expectedDate, null, partner))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Event total spots must not be null");
  }

  @Test
  @DisplayName("Não deve instanciar um evento sem parceiro")
  void test5() {
    final var expectedName = "Evento 1";
    final var expectedDate = "2023-10-05";
    final var expectedTotalSpots = 10;

    Assertions.assertThatThrownBy(() -> Event.newEvent(expectedName, expectedDate, expectedTotalSpots, null))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner must not be null");
  }

  @Test
  @DisplayName("Deve reservar um ticket quando for possível")
  void test6() {

    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "Evento 1";
    final var expectedDate = "2023-12-05";
    final var expectedTotalSpots = 10;
    final var expectedTickets = 1;

    final var event = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

    final var customer = Customer.newCustomer("Cliente 1", "123.456.789-00", "cliente@gmail.com");

    final var expectedCustomerId = customer.customerId();
    final var expectedEventId = event.eventId();
    final var expectedPartnerId = partner.partnerId();

    final var ticket = event.reserveTicket(expectedCustomerId);

    Assertions.assertThat(event.eventId()).isNotNull();
    Assertions.assertThat(event.name().value()).isEqualTo(expectedName);
    Assertions.assertThat(event.date()).isEqualTo(expectedDate);
    Assertions.assertThat(event.totalSpots()).isEqualTo(expectedTotalSpots);
    Assertions.assertThat(event.partnerId()).isEqualTo(expectedPartnerId);
    Assertions.assertThat(event.tickets()).hasSize(expectedTickets);

    Assertions.assertThat(ticket.ticketId()).isNotNull();
    Assertions.assertThat(ticket.customerId()).isEqualTo(expectedCustomerId);
    Assertions.assertThat(ticket.eventId()).isEqualTo(expectedEventId);
    Assertions.assertThat(ticket.status()).isEqualTo(TicketStatus.PENDING);
    Assertions.assertThat(ticket.reservedAt()).isNotNull();
    Assertions.assertThat(ticket.paidAt()).isNull();

    final var actualEventTicket = event.tickets().iterator().next();
    Assertions.assertThat(actualEventTicket.eventId()).isEqualTo(expectedEventId);
    Assertions.assertThat(actualEventTicket.customerId()).isEqualTo(expectedCustomerId);
    Assertions.assertThat(actualEventTicket.ticketId()).isEqualTo(ticket.ticketId());
    Assertions.assertThat(actualEventTicket.ordering()).isEqualTo(expectedTickets);
  }

  @Test
  @DisplayName("Não deve reservar dois tickets para o mesmo cliente")
  void test7() {

    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "Evento 1";
    final var expectedDate = "2023-12-05";
    final var expectedTotalSpots = 10;

    final var event = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

    final var customer = Customer.newCustomer("Cliente 1", "123.456.789-00", "cliente@gmail.com");

    event.reserveTicket(customer.customerId());

    Assertions.assertThatThrownBy(() -> event.reserveTicket(customer.customerId()))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Email already registered");
  }

  @Test
  @DisplayName("Não deve reservar um ticket quando o evento estiver esgotado")
  void test8() {

    final var partner = Partner.newPartner("Fulano", "12.345.678/0009-01", "fulano@gmail.com");
    final var expectedName = "Evento 1";
    final var expectedDate = "2023-12-05";
    final var expectedTotalSpots = 1;

    final var event = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

    final var customer = Customer.newCustomer("Cliente 1", "123.456.789-00", "cliente@gmail.com");
    final var anotherCustomer = Customer.newCustomer("Cliente 1", "123.456.789-00", "outro.cliente@gmail.com");

    event.reserveTicket(customer.customerId());

    Assertions.assertThatThrownBy(() -> event.reserveTicket(anotherCustomer.customerId()))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Event sold out");

  }

}
