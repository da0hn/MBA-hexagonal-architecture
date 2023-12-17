package br.com.fullcycle.infrastructure.rest;

import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.infrastructure.dtos.SubscribeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/events")
public class EventController {

  private final CreateEventUseCase createEventUseCase;

  private final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

  public EventController(
    final CreateEventUseCase createEventUseCase,
    final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase
  ) {
    this.createEventUseCase = createEventUseCase;
    this.subscribeCustomerToEventUseCase = subscribeCustomerToEventUseCase;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody final NewEventDTO dto) {
    try {
      final var output = this.createEventUseCase.execute(new CreateEventUseCase.Input(
        dto.date(),
        dto.name(),
        dto.partnerId(),
        dto.totalSpots()
      ));
      return ResponseEntity.created(URI.create("/events/" + output.id())).body(output);
    }
    catch (final Exception e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

  @PostMapping(value = "/{id}/subscribe")
  public ResponseEntity<?> subscribe(@PathVariable final String id, @RequestBody final SubscribeDTO dto) {
    try {
      final var output = this.subscribeCustomerToEventUseCase.execute(new SubscribeCustomerToEventUseCase.Input(
        id,
        dto.customerId()
      ));
      return ResponseEntity.ok(output);
    }
    catch (final Exception e) {
      return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
  }

}
