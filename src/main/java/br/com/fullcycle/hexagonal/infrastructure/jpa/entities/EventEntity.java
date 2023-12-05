package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
public class EventEntity {

  @Id
  private UUID id;

  private String name;

  private LocalDate date;

  private int totalSpots;

  @ManyToOne(fetch = FetchType.LAZY)
  private PartnerEntity partner;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.EAGER)
  private Set<TicketEntity> tickets;

  public EventEntity() {
    this.tickets = new HashSet<>();
  }

  public EventEntity(final UUID id, final String name, final LocalDate date, final int totalSpots, final Set<TicketEntity> tickets) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.totalSpots = totalSpots;
    this.tickets = tickets != null ? tickets : new HashSet<>();
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

  public PartnerEntity getPartner() {
    return this.partner;
  }

  public void setPartner(final PartnerEntity partner) {
    this.partner = partner;
  }

  public Set<TicketEntity> getTickets() {
    return this.tickets;
  }

  public void setTickets(final Set<TicketEntity> tickets) {
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

}
