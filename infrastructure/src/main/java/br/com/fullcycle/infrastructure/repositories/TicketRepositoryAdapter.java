package br.com.fullcycle.infrastructure.repositories;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.ticket.Ticket;
import br.com.fullcycle.domain.ticket.TicketId;
import br.com.fullcycle.domain.ticket.TicketRepository;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.TicketJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class TicketRepositoryAdapter implements TicketRepository {

  private final TicketJpaRepository ticketJpaRepository;

  private final OutboxJpaRepository outboxJpaRepository;

  private final ObjectMapper objectMapper;

  public TicketRepositoryAdapter(
    final TicketJpaRepository ticketJpaRepository,
    final OutboxJpaRepository outboxJpaRepository,
    final ObjectMapper objectMapper
  ) {
    this.ticketJpaRepository = ticketJpaRepository;
    this.outboxJpaRepository = outboxJpaRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public Optional<Ticket> ticketOfId(final TicketId ticketId) {
    return this.ticketJpaRepository.findById(ticketId.value())
      .map(TicketEntity::toTicket);
  }

  @Override
  @Transactional
  public Ticket create(final Ticket ticket) {
    return this.save(ticket);
  }

  @Override
  @Transactional
  public Ticket update(final Ticket ticket) {
    return this.save(ticket);
  }

  @Override
  public void deleteAll() {
    this.ticketJpaRepository.deleteAll();
  }

  private Ticket save(final Ticket ticket) {
    this.outboxJpaRepository.saveAll(
      ticket.domainEvents().stream()
        .map(it -> OutboxEntity.of(it, this::toJson))
        .toList()
    );
    return this.ticketJpaRepository.save(TicketEntity.of(ticket)).toTicket();
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
