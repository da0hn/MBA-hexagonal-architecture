package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

class CreateEventUseCaseTest {

  @Test
  @DisplayName("Deve criar um evento")
  void testCreate() {

    final var eventService = Mockito.mock(EventService.class);
    final var partnerService = Mockito.mock(PartnerService.class);

    final var expectedEventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedPartnerId = UUID.randomUUID().getMostSignificantBits();
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";
    final var expectedTotalSpots = 100;

    final var partner = new Partner();
    partner.setId(expectedPartnerId);

    Mockito.doReturn(Optional.of(partner))
      .when(partnerService)
      .findById(expectedPartnerId);
    Mockito.doAnswer(a -> {
        final var event = a.getArgument(0, Event.class);
        event.setId(expectedEventId);
        return event;
      })
      .when(eventService)
      .save(Mockito.any());

    final var createEventUseCase = new CreateEventUseCase(eventService, partnerService);
    final var output = createEventUseCase.execute(
      new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots)
    );

    Assertions.assertThat(output.id()).isEqualTo(expectedEventId);
    Assertions.assertThat(output.date()).isEqualTo(expectedDate);
    Assertions.assertThat(output.name()).isEqualTo(expectedName);
    Assertions.assertThat(output.partnerId()).isEqualTo(expectedPartnerId);
    Assertions.assertThat(output.totalSpots()).isEqualTo(expectedTotalSpots);
  }

  @Test
  @DisplayName("Não deve cadastrar um evento quando o parceiro não for encontrado")
  void testCreateEvent_whenPartnerDoesntExists_shouldThrowException() {
    final var eventService = Mockito.mock(EventService.class);
    final var partnerService = Mockito.mock(PartnerService.class);

    final var expectedPartnerId = UUID.randomUUID().getMostSignificantBits();
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";

    Mockito.doReturn(Optional.empty())
      .when(partnerService)
      .findById(expectedPartnerId);

    final var input = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, 100);

    final var createEventUseCase = new CreateEventUseCase(eventService, partnerService);

    Assertions.assertThatThrownBy(() -> createEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner not found");
  }

}
