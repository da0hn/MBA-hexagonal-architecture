package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.infrastructure.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateEventUseCaseIT extends IntegrationTest {

  @Autowired
  private CreateEventUseCase createEventUseCase;

  @Autowired
  private PartnerRepository partnerRepository;

  @Autowired
  private EventRepository eventRepository;

  private Partner createPartner(final String cnpj, final String email, final String name) {
    return this.partnerRepository.create(Partner.newPartner(name, cnpj, email));
  }

  @AfterEach
  void tearDown() {
    this.eventRepository.deleteAll();
    this.partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um evento")
  void testCreate() {
    final var partner = this.createPartner("12.345.678/9012-34", "partner@gmail.com", "Partner");
    final var expectedPartnerId = partner.partnerId().asString();
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";
    final var expectedTotalSpots = 100;

    final var output = this.createEventUseCase.execute(
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
    final var expectedDate = "2021-01-01";
    final var expectedName = "Disney on Ice";

    final var input = new CreateEventUseCase.Input(expectedDate, expectedName, PartnerId.unique().asString(), 100);

    Assertions.assertThatThrownBy(() -> this.createEventUseCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner not found");
  }

}
