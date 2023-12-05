package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryEventRepository implements EventRepository {

  private final Map<String, Event> events;

  public InMemoryEventRepository() {
    this.events = new HashMap<>();
  }

  @Override
  public Optional<Event> eventOfId(final EventId eventId) {
    return Optional.ofNullable(this.events.getOrDefault(eventId.asString(), null));
  }

  @Override
  public Event create(final Event event) {
    this.events.put(event.eventId().asString(), event);
    return event;
  }

  @Override
  public Event update(final Event event) {
    this.events.put(event.eventId().asString(), event);
    return event;
  }

  @Override
  public void deleteAll() {
    this.events.clear();
  }

}
