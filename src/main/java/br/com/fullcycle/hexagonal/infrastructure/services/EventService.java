package br.com.fullcycle.hexagonal.infrastructure.services;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.TicketJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private EventJpaRepository eventRepository;

  @Autowired
  private TicketJpaRepository ticketRepository;

  @Transactional
  public EventEntity save(final EventEntity event) {
    return this.eventRepository.save(event);
  }

  public Optional<EventEntity> findById(final Long id) {
    return this.eventRepository.findById(id);
  }

  public Optional<TicketEntity> findTicketByEventIdAndCustomerId(final Long id, final Long customerId) {
    return this.ticketRepository.findByEventIdAndCustomerId(id, customerId);
  }

}
