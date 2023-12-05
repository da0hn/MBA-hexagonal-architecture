package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.domain.Entities;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.domain.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Event {

  private final EventId eventId;

  private final Set<EventTicket> tickets;

  private Name name;

  private LocalDate date;

  private int totalSpots;

  private PartnerId partnerId;

  public Event(final EventId eventId, final Name name, final LocalDate date, final Integer totalSpots, final PartnerId partnerId) {
    this(eventId);
    this.name = Entities.requireNonNull(name, "Event name must not be null");
    this.date = Entities.requireNonNull(date, "Event date must not be null");
    this.totalSpots = Entities.requireNonNull(totalSpots, "Event total spots must not be null");
    this.partnerId = Entities.requireNonNull(partnerId, "Event partner id must not be null");
  }

  private Event(final EventId eventId) {
    this.eventId = Entities.requireNonNull(eventId, "Event id must not be null");
    this.tickets = new HashSet<>(0);
  }

  public static Event newEvent(final String name, final CharSequence date, final Integer totalSpots, final Partner partner) {
    Entities.requireNonNull(partner, "Partner must not be null");
    Entities.requireNonNull(date, "Date must not be null");
    return new Event(
      EventId.unique(),
      new Name(name),
      LocalDate.parse(date, DateTimeFormatter.ISO_DATE),
      totalSpots,
      partner.partnerId()
    );
  }

  public Set<EventTicket> tickets() {
    return Collections.unmodifiableSet(this.tickets);
  }

  public Name name() {
    return this.name;
  }

  public LocalDate date() {
    return this.date;
  }

  public int totalSpots() {
    return this.totalSpots;
  }

  public PartnerId partnerId() {
    return this.partnerId;
  }

  public EventId eventId() {
    return this.eventId;
  }

  public Ticket reserveTicket(final CustomerId customerId) {

    this.tickets().stream()
      .filter(it -> it.customerId().equals(customerId))
      .findFirst()
      .ifPresent(ignoredTicket -> {
        throw new ValidationException("Email already registered");
      });

    if (this.totalSpots < this.tickets.size() + 1) {
      throw new ValidationException("Event sold out");
    }

    final var ticket = Ticket.newTicket(customerId, this.eventId);
    this.tickets.add(new EventTicket(ticket.ticketId(), this.eventId, customerId, this.tickets.size() + 1));
    return ticket;
  }

  @Override
  public int hashCode() {
    return this.eventId.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof final Event event)) return false;

    return this.eventId.equals(event.eventId);
  }

}
