package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import br.com.fullcycle.hexagonal.application.domain.ticket.TicketId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity(name = "EventTicket")
@Table(name = "event_tickets")
public class EventTicketEntity {

  @Id
  private UUID ticketId;

  private UUID customerId;

  private Integer ordering;

  @ManyToOne(fetch = FetchType.LAZY)
  private EventEntity event;

  public EventTicketEntity() {
  }

  public EventTicketEntity(final UUID ticketId, final UUID customerId, final Integer ordering, final EventEntity event) {
    this.ticketId = ticketId;
    this.customerId = customerId;
    this.ordering = ordering;
    this.event = event;
  }

  public static EventTicketEntity of(final EventEntity event, final EventTicket eventTicket) {
    return new EventTicketEntity(
      eventTicket.ticketId().value(),
      eventTicket.customerId().value(),
      eventTicket.ordering(),
      event
    );
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
  public int hashCode() {
    int result = this.ticketId.hashCode();
    result = 31 * result + this.customerId.hashCode();
    result = 31 * result + this.ordering.hashCode();
    result = 31 * result + this.event.hashCode();
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof final EventTicketEntity that)) return false;

    if (!this.ticketId.equals(that.ticketId)) return false;
    if (!this.customerId.equals(that.customerId)) return false;
    if (!this.ordering.equals(that.ordering)) return false;
    return this.event.equals(that.event);
  }

  public EventTicket toEventTicket() {
    return new EventTicket(
      new TicketId(this.ticketId),
      new EventId(this.event.getId()),
      new CustomerId(this.customerId),
      this.ordering
    );
  }

}
