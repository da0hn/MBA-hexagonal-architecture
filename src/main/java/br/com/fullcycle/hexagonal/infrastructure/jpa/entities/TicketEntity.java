package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.ticket.TicketStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class TicketEntity {

  @Id
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  private CustomerEntity customer;

  @ManyToOne(fetch = FetchType.LAZY)
  private EventEntity event;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  private Instant paidAt;

  private Instant reservedAt;

  public TicketEntity() {
  }

  public TicketEntity(
    final UUID id,
    final CustomerEntity customer,
    final EventEntity event,
    final TicketStatus status,
    final Instant paidAt,
    final Instant reservedAt
  ) {
    this.id = id;
    this.customer = customer;
    this.event = event;
    this.status = status;
    this.paidAt = paidAt;
    this.reservedAt = reservedAt;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public CustomerEntity getCustomer() {
    return this.customer;
  }

  public void setCustomer(final CustomerEntity customer) {
    this.customer = customer;
  }

  public EventEntity getEvent() {
    return this.event;
  }

  public void setEvent(final EventEntity event) {
    this.event = event;
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
    return Objects.hash(this.customer, this.event);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final TicketEntity ticket = (TicketEntity) o;
    return Objects.equals(this.customer, ticket.customer) && Objects.equals(this.event, ticket.event);
  }

}
