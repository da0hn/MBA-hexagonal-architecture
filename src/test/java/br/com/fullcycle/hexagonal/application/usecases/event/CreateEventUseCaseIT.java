package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class CreateEventUseCaseIT extends IntegrationTest {

  @Autowired
  private CreateEventUseCase createEventUseCase;

  @Autowired
  private PartnerJpaRepository partnerRepository;

  @Autowired
  private EventJpaRepository eventRepository;

  private PartnerEntity createPartner(final String cnpj, final String email, final String name) {
    final var partner = new PartnerEntity();
    partner.setCnpj(cnpj);
    partner.setEmail(email);
    partner.setName(name);
    return this.partnerRepository.save(partner);
  }

  @AfterEach
  void tearDown() {
    this.eventRepository.deleteAll();
    this.partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um evento")
  void testCreate() {
    final var partner = this.createPartner("12345678901234", "partner@gmail.com", "Partner");
    final UUID expectedPartnerId = partner.getId();
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";
    final var expectedTotalSpots = 100;

    final var output = this.createEventUseCase.execute(
      new CreateEventUseCase.Input(expectedDate, expectedName, null, expectedTotalSpots)
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
    final var expectedPartnerId = UUID.randomUUID().getMostSignificantBits();
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";

    final var input = new CreateEventUseCase.Input(expectedDate, expectedName, null, 100);

    Assertions.assertThatThrownBy(() -> this.createEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner not found");
  }

}
