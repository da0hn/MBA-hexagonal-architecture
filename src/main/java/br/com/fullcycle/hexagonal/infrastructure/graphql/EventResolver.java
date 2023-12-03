package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EventResolver {

  private final CreateEventUseCase createEventUseCase;

  private final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase;

  public EventResolver(
    final CreateEventUseCase createEventUseCase,
    final SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase
  ) {
    this.createEventUseCase = createEventUseCase;
    this.subscribeCustomerToEventUseCase = subscribeCustomerToEventUseCase;
  }

  @MutationMapping
  public CreateEventUseCase.Output createEvent(@Argument final NewEventDTO input) {
    final var output = this.createEventUseCase.execute(new CreateEventUseCase.Input(
      input.date(),
      input.name(),
      input.partnerId(),
      input.totalSpots()
    ));
    return output;
  }

  @MutationMapping
  public SubscribeCustomerToEventUseCase.Output subscribeCustomerToEvent(@Argument final SubscribeDTO input) {
    final var output = this.subscribeCustomerToEventUseCase.execute(new SubscribeCustomerToEventUseCase.Input(
      input.eventId(),
      input.customerId()
    ));
    return output;
  }

}
