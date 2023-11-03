package br.com.fullcycle.hexagonal.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tickets")
public class Ticket {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  private Event event;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  private Instant paidAt;

  private Instant reservedAt;

  public Ticket() {
  }

  public Ticket(final Long id, final Customer customer, final Event event, final TicketStatus status, final Instant paidAt, final Instant reservedAt) {
    this.id = id;
    this.customer = customer;
    this.event = event;
    this.status = status;
    this.paidAt = paidAt;
    this.reservedAt = reservedAt;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public void setCustomer(final Customer customer) {
    this.customer = customer;
  }

  public Event getEvent() {
    return this.event;
  }

  public void setEvent(final Event event) {
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
    final Ticket ticket = (Ticket) o;
    return Objects.equals(this.customer, ticket.customer) && Objects.equals(this.event, ticket.event);
  }

}
