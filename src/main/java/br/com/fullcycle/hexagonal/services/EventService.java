package br.com.fullcycle.hexagonal.services;

import br.com.fullcycle.hexagonal.models.Event;
import br.com.fullcycle.hexagonal.models.Ticket;
import br.com.fullcycle.hexagonal.repositories.EventRepository;
import br.com.fullcycle.hexagonal.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private TicketRepository ticketRepository;

  @Transactional
  public Event save(final Event event) {
    return this.eventRepository.save(event);
  }

  public Optional<Event> findById(final Long id) {
    return this.eventRepository.findById(id);
  }

  public Optional<Ticket> findTicketByEventIdAndCustomerId(final Long id, final Long customerId) {
    return this.ticketRepository.findByEventIdAndCustomerId(id, customerId);
  }

}
