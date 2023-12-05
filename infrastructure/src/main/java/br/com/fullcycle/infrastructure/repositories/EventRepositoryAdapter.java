package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.EventJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventRepositoryAdapter implements EventRepository {

  private final EventJpaRepository eventJpaRepository;

  public EventRepositoryAdapter(final EventJpaRepository eventJpaRepository) { this.eventJpaRepository = eventJpaRepository; }

  @Override
  public Optional<Event> eventOfId(final EventId eventId) {
    return this.eventJpaRepository.findById(eventId.value())
      .map(EventEntity::toEvent);
  }

  @Override
  public Event create(final Event event) {
    return this.eventJpaRepository.save(EventEntity.of(event)).toEvent();
  }

  @Override
  public Event update(final Event event) {
    return this.eventJpaRepository.save(EventEntity.of(event)).toEvent();
  }

  @Override
  public void deleteAll() {
    this.eventJpaRepository.deleteAll();
  }

}
