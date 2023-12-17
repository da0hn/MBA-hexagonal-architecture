package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.Entities;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Name;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Event {

  private final EventId eventId;

  private final Set<EventTicket> tickets;

  private final Set<DomainEvent> domainEvents;

  private Name name;

  private LocalDate date;

  private int totalSpots;

  private PartnerId partnerId;

  public Event(
    final EventId eventId,
    final Name name,
    final LocalDate date,
    final Integer totalSpots,
    final PartnerId partnerId,
    final Collection<EventTicket> tickets
  ) {
    this(eventId, tickets);
    this.name = Entities.requireNonNull(name, "Event name must not be null");
    this.date = Entities.requireNonNull(date, "Event date must not be null");
    this.totalSpots = Entities.requireNonNull(totalSpots, "Event total spots must not be null");
    this.partnerId = Entities.requireNonNull(partnerId, "Event partner id must not be null");
  }

  private Event(final EventId eventId, final Collection<EventTicket> tickets) {
    this.eventId = Entities.requireNonNull(eventId, "Event id must not be null");
    this.tickets = tickets == null ? new HashSet<>(0) : new HashSet<>(tickets);
    this.domainEvents = new HashSet<>(0);
  }

  public static Event newEvent(final String name, final CharSequence date, final Integer totalSpots, final Partner partner) {
    Entities.requireNonNull(partner, "Partner must not be null");
    Entities.requireNonNull(date, "Date must not be null");
    return new Event(
      EventId.unique(),
      new Name(name),
      LocalDate.parse(date, DateTimeFormatter.ISO_DATE),
      totalSpots,
      partner.partnerId(),
      null
    );
  }

  public static Event restore(
    final String eventId,
    final String name,
    final LocalDate date,
    final int totalSpots,
    final String partnerId,
    final List<EventTicket> tickets
  ) {
    return new Event(
      EventId.with(eventId),
      new Name(name),
      date,
      totalSpots,
      PartnerId.with(partnerId),
      tickets
    );
  }

  public Set<EventTicket> tickets() {
    return Collections.unmodifiableSet(this.tickets);
  }

  public Set<DomainEvent> domainEvents() {
    return Collections.unmodifiableSet(this.domainEvents);
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

  public EventTicket reserveTicket(final CustomerId customerId) {

    this.tickets().stream()
      .filter(it -> it.customerId().equals(customerId))
      .findFirst()
      .ifPresent(ignoredTicket -> {
        throw new ValidationException("Email already registered");
      });

    if (this.totalSpots < this.tickets.size() + 1) {
      throw new ValidationException("Event sold out");
    }
    final var eventTicket = EventTicket.newTicket(this.eventId, customerId, this.tickets.size() + 1);
    this.tickets.add(eventTicket);
    this.domainEvents.add(new EventTicketReserved(eventTicket.eventTicketId(), this.eventId, customerId));
    return eventTicket;
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
