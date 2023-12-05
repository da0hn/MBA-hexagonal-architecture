package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Event")
@Table(name = "events")
public class EventEntity {

  @Id
  private UUID id;

  private String name;

  private LocalDate date;

  private int totalSpots;

  private UUID partnerId;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.EAGER)
  private Set<EventTicketEntity> tickets;

  public EventEntity() {
    this.tickets = new HashSet<>();
  }

  public EventEntity(
    final UUID id,
    final String name,
    final LocalDate date,
    final int totalSpots,
    final UUID partnerId
  ) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.totalSpots = totalSpots;
    this.partnerId = partnerId;
  }

  public static EventEntity of(final Event event) {
    final var instance = new EventEntity(
      event.eventId().value(),
      event.name().value(),
      event.date(),
      event.totalSpots(),
      event.partnerId().value()
    );

    event.tickets().forEach(instance::addTicket);

    return instance;
  }

  public void addTicket(final EventTicket eventTicket) {
    this.tickets.add(EventTicketEntity.of(this, eventTicket));
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public LocalDate getDate() {
    return this.date;
  }

  public void setDate(final LocalDate date) {
    this.date = date;
  }

  public int getTotalSpots() {
    return this.totalSpots;
  }

  public void setTotalSpots(final int totalSpots) {
    this.totalSpots = totalSpots;
  }

  public UUID getPartnerId() {
    return this.partnerId;
  }

  public void setPartnerId(final UUID partnerId) {
    this.partnerId = partnerId;
  }

  public Set<EventTicketEntity> getTickets() {
    return this.tickets;
  }

  public void setTickets(final Set<EventTicketEntity> tickets) {
    this.tickets = tickets;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    final EventEntity event = (EventEntity) o;
    return Objects.equals(this.id, event.id);
  }

  public Event toEvent() {
    return Event.restore(
      this.id.toString(),
      this.name,
      this.date,
      this.totalSpots,
      this.partnerId.toString(),
      this.tickets.stream().map(EventTicketEntity::toEventTicket).toList()
    );
  }

}
