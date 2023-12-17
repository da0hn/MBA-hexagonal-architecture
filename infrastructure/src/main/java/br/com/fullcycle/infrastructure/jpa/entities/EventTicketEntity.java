package br.com.fullcycle.infrastructure.jpa.entities;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicket;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.ticket.TicketId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Optional;
import java.util.UUID;

@Entity(name = "EventTicket")
@Table(name = "event_tickets")
public class EventTicketEntity {

  @Id
  private UUID eventTicketId;

  private UUID ticketId;

  private UUID customerId;

  private Integer ordering;

  @ManyToOne(fetch = FetchType.LAZY)
  private EventEntity event;

  public EventTicketEntity() {
  }

  public EventTicketEntity(
    final UUID eventTicketId,
    final UUID customerId,
    final UUID ticketId,
    final Integer ordering,
    final EventEntity event
  ) {
    this.eventTicketId = eventTicketId;
    this.customerId = customerId;
    this.ticketId = ticketId;
    this.ordering = ordering;
    this.event = event;
  }

  public static EventTicketEntity of(final EventEntity event, final EventTicket eventTicket) {
    return new EventTicketEntity(
      eventTicket.eventTicketId().value(),
      eventTicket.customerId().value(),
      Optional.ofNullable(eventTicket.ticketId()).map(TicketId::value).orElse(null),
      eventTicket.ordering(),
      event
    );
  }

  public UUID getEventTicketId() {
    return this.eventTicketId;
  }

  public void setEventTicketId(final UUID eventTicketId) {
    this.eventTicketId = eventTicketId;
  }

  public UUID getTicketId() {
    return this.ticketId;
  }

  public void setTicketId(final UUID ticketId) {
    this.ticketId = ticketId;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(final UUID customerId) {
    this.customerId = customerId;
  }

  public EventEntity getEvent() {
    return this.event;
  }

  public void setEvent(final EventEntity event) {
    this.event = event;
  }

  public Integer getOrdering() {
    return this.ordering;
  }

  public void setOrdering(final Integer ordering) {
    this.ordering = ordering;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof EventTicketEntity that)) return false;

    return this.eventTicketId.equals(that.eventTicketId);
  }

  @Override
  public int hashCode() {
    return this.eventTicketId.hashCode();
  }

  public EventTicket toEventTicket() {
    return new EventTicket(
      new EventTicketId(this.eventTicketId),
      Optional.ofNullable(this.ticketId).map(TicketId::new).orElse(null),
      new EventId(this.event.getId()),
      new CustomerId(this.customerId),
      this.ordering
    );
  }

}
