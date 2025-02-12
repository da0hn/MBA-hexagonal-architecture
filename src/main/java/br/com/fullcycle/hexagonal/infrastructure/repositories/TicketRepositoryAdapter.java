package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.TicketJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class TicketRepositoryAdapter implements TicketRepository {

  private final TicketJpaRepository ticketJpaRepository;

  public TicketRepositoryAdapter(final TicketJpaRepository ticketJpaRepository) { this.ticketJpaRepository = ticketJpaRepository; }

  @Override
  public Optional<Ticket> ticketOfId(final TicketId ticketId) {
    return this.ticketJpaRepository.findById(ticketId.value())
      .map(TicketEntity::toTicket);
  }

  @Override
  @Transactional
  public Ticket create(final Ticket ticket) {
    return this.ticketJpaRepository.save(TicketEntity.of(ticket)).toTicket();
  }

  @Override
  @Transactional
  public Ticket update(final Ticket ticket) {
    return this.ticketJpaRepository.save(TicketEntity.of(ticket)).toTicket();
  }

  @Override
  public void deleteAll() {
    this.ticketJpaRepository.deleteAll();
  }

}
