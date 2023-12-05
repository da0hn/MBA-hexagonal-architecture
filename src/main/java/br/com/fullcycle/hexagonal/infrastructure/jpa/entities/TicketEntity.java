package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.domain.ticket.TicketStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "Ticket")
@Table(name = "tickets")
public class TicketEntity {

  @Id
  private UUID id;

  private UUID customerId;

  private UUID eventId;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  private Instant paidAt;

  private Instant reservedAt;

  public TicketEntity() {
  }

  public TicketEntity(
    final UUID id,
    final UUID customerId,
    final UUID eventId,
    final TicketStatus status,
    final Instant paidAt,
    final Instant reservedAt
  ) {
    this.id = id;
    this.customerId = customerId;
    this.eventId = eventId;
    this.status = status;
    this.paidAt = paidAt;
    this.reservedAt = reservedAt;
  }

  public static TicketEntity of(final Ticket ticket) {
    return new TicketEntity(
      ticket.ticketId().value(),
      ticket.customerId().value(),
      ticket.eventId().value(),
      ticket.status(),
      ticket.paidAt(),
      ticket.reservedAt()
    );
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(final UUID customerId) {
    this.customerId = customerId;
  }

  public UUID getEventId() {
    return this.eventId;
  }

  public void setEventId(final UUID eventId) {
    this.eventId = eventId;
  }

  public TicketStatus getStatus() {
    return this.status;
  }

  public void setStatus(final TicketStatus status) {
    this.status = status;
  }

  public Instant getPaidAt() {
    return this.paidAt;
  }

  public void setPaidAt(final Instant paidAt) {
    this.paidAt = paidAt;
  }

  public Instant getReservedAt() {
    return this.reservedAt;
  }

  public void setReservedAt(final Instant reservedAt) {
    this.reservedAt = reservedAt;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof final TicketEntity that)) return false;

    return this.id.equals(that.id);
  }

  public Ticket toTicket() {
    return new Ticket(
      new TicketId(this.id),
      new CustomerId(this.customerId),
      new EventId(this.eventId),
      this.status,
      this.paidAt,
      this.reservedAt
    );
  }

}
