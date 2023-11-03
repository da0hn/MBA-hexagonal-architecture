package br.com.fullcycle.hexagonal.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "events")
public class Event {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String name;

  private LocalDate date;

  private int totalSpots;

  @ManyToOne(fetch = FetchType.LAZY)
  private Partner partner;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
  private Set<Ticket> tickets;

  public Event() {
    this.tickets = new HashSet<>();
  }

  public Event(final Long id, final String name, final LocalDate date, final int totalSpots, final Set<Ticket> tickets) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.totalSpots = totalSpots;
    this.tickets = tickets != null ? tickets : new HashSet<>();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(final Long id) {
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

  public Partner getPartner() {
    return this.partner;
  }

  public void setPartner(final Partner partner) {
    this.partner = partner;
  }

  public Set<Ticket> getTickets() {
    return this.tickets;
  }

  public void setTickets(final Set<Ticket> tickets) {
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
    final Event event = (Event) o;
    return Objects.equals(this.id, event.id);
  }

}
