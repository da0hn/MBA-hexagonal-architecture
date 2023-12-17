package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventRepositoryAdapter implements EventRepository {

  private final EventJpaRepository eventJpaRepository;

  private final OutboxJpaRepository outboxJpaRepository;

  private final ObjectMapper objectMapper;

  public EventRepositoryAdapter(
    final EventJpaRepository eventJpaRepository,
    final OutboxJpaRepository outboxJpaRepository,
    final ObjectMapper objectMapper
  ) {
    this.eventJpaRepository = eventJpaRepository;
    this.outboxJpaRepository = outboxJpaRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public Optional<Event> eventOfId(final EventId eventId) {
    return this.eventJpaRepository.findById(eventId.value())
      .map(EventEntity::toEvent);
  }

  @Override
  public Event create(final Event event) {
    return this.save(event);
  }

  @Override
  public Event update(final Event event) {
    return this.save(event);
  }

  @Override
  public void deleteAll() {
    this.eventJpaRepository.deleteAll();
  }

  private Event save(final Event event) {
    this.outboxJpaRepository.saveAll(
      event.domainEvents().stream()
        .map(it -> OutboxEntity.of(it, this::toJson))
        .toList()
    );
    return this.eventJpaRepository.save(EventEntity.of(event)).toEvent();
  }

  private String toJson(final DomainEvent domainEvent) {
    try {
      return this.objectMapper.writeValueAsString(domainEvent);
    }
    catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
