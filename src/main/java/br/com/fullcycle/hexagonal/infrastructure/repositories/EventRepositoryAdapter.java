package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
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

}
