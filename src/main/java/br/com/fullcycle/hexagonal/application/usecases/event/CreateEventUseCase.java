package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.application.usecases.UseCase;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

import java.util.Objects;

public class CreateEventUseCase extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

  private final EventRepository eventRepository;

  private final PartnerRepository partnerRepository;

  public CreateEventUseCase(
    final EventRepository eventRepository,
    final PartnerRepository partnerRepository
  ) {
    this.eventRepository = Objects.requireNonNull(eventRepository);
    this.partnerRepository = Objects.requireNonNull(partnerRepository);
  }

  public Output execute(final Input input) {
    final var partner = this.partnerRepository.partnerOfId(PartnerId.with(input.partnerId()))
      .orElseThrow(() -> new ValidationException("Partner not found"));

    final var event = Event.newEvent(input.name(), input.date(), input.totalSpots(), partner);

    this.eventRepository.create(event);

    return new Output(
      event.eventId().asString(),
      input.date(),
      event.name().value(),
      input.partnerId(),
      input.totalSpots()
    );
  }

  public record Input(String date, String name, String partnerId, Integer totalSpots) { }

  public record Output(String id, String date, String name, String partnerId, Integer totalSpots) { }

}
