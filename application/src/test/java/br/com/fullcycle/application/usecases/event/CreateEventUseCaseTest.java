package br.com.fullcycle.application.usecases.event;

import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.application.repository.InMemoryEventRepository;
import br.com.fullcycle.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CreateEventUseCaseTest {

  @Test
  @DisplayName("Deve criar um evento")
  void testCreate() {

    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";
    final var expectedTotalSpots = 100;

    final var partner = Partner.newPartner("Partner 1", "12.345.678/0009-00", "partner@gmail.com");
    final var expectedPartnerId = partner.partnerId().asString();

    final var partnerRepository = new InMemoryPartnerRepository();
    partnerRepository.create(partner);

    final var createEventUseCase = new CreateEventUseCase(new InMemoryEventRepository(), partnerRepository);
    final var output = createEventUseCase.execute(
      new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots)
    );

    Assertions.assertThat(output.id()).isNotNull();
    Assertions.assertThat(output.date()).isEqualTo(expectedDate);
    Assertions.assertThat(output.name()).isEqualTo(expectedName);
    Assertions.assertThat(output.partnerId()).isEqualTo(expectedPartnerId);
    Assertions.assertThat(output.totalSpots()).isEqualTo(expectedTotalSpots);
  }

  @Test
  @DisplayName("Não deve cadastrar um evento quando o parceiro não for encontrado")
  void testCreateEvent_whenPartnerDoesntExists_shouldThrowException() {
    final var eventRepository = new InMemoryEventRepository();
    final var partnerRepository = new InMemoryPartnerRepository();

    final var expectedPartnerId = UUID.randomUUID().toString();
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";

    final var input = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, 100);

    final var createEventUseCase = new CreateEventUseCase(eventRepository, partnerRepository);

    Assertions.assertThatThrownBy(() -> createEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner not found");
  }

}
