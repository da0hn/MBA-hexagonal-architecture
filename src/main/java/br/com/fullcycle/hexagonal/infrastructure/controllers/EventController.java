package br.com.fullcycle.hexagonal.infrastructure.controllers;

import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.EventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(value = "events")
public class EventController {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private EventService eventService;

  @Autowired
  private PartnerService partnerService;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody final EventDTO dto) {
    try {
      final var partnerId = Objects.requireNonNull(dto.getPartner(), "Partner is required").getId();
      final var createEventUseCase = new CreateEventUseCase(this.eventService, this.partnerService);
      final var output = createEventUseCase.execute(new CreateEventUseCase.Input(
        dto.getDate(),
        dto.getName(),
        partnerId,
        dto.getTotalSpots()
      ));
      return ResponseEntity.created(URI.create("/events/" + output.id())).body(output);
    }
    catch (final Exception e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @Transactional
  @PostMapping(value = "/{id}/subscribe")
  public ResponseEntity<?> subscribe(@PathVariable final Long id, @RequestBody final SubscribeDTO dto) {
    try {
      final var subscribeCustomerToEventUseCase = new SubscribeCustomerToEventUseCase(this.eventService, this.customerService);
      final var output = subscribeCustomerToEventUseCase.execute(new SubscribeCustomerToEventUseCase.Input(
        id,
        dto.getCustomerId()
      ));
      return ResponseEntity.ok(output);
    }
    catch (final Exception e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

}
