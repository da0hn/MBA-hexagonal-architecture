package br.com.fullcycle.hexagonal.infrastructure.dtos;

import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;

import java.time.Instant;

public class TicketDTO {

  private Long id;

  private int spot;

  private CustomerDTO customer;

  private EventDTO event;

  private TicketStatus status;

  private Instant paidAt;

  private Instant reservedAt;

  public TicketDTO() {
  }

  public TicketDTO(final Ticket ticket) {
    this.id = ticket.getId();
    this.customer = new CustomerDTO(ticket.getCustomer());
    this.event = new EventDTO(ticket.getEvent());
    this.status = ticket.getStatus();
    this.paidAt = ticket.getPaidAt();
    this.reservedAt = ticket.getReservedAt();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public int getSpot() {
    return this.spot;
  }

  public void setSpot(final int spot) {
    this.spot = spot;
  }

  public CustomerDTO getCustomer() {
    return this.customer;
  }

  public void setCustomer(final CustomerDTO customer) {
    this.customer = customer;
  }

  public EventDTO getEvent() {
    return this.event;
  }

  public void setEvent(final EventDTO event) {
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

}
