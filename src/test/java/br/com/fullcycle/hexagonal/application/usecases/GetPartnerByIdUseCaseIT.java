package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class GetPartnerByIdUseCaseIT extends IntegrationTest {

  @Autowired
  private GetPartnerByIdUseCase useCase;

  @Autowired
  private PartnerRepository partnerRepository;

  @AfterEach
  void tearDown() {
    this.partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve obter um parceiro por id")
  void testGetById() {
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedCnpj = "12345678900";

    final var aPartner = new Partner(null, expectedName, expectedCnpj, expectedEmail);
    this.partnerRepository.save(aPartner);

    final var expectedId = aPartner.getId();

    final var output = this.useCase.execute(new GetPartnerByIdUseCase.Input(expectedId));

    Assertions.assertThat(output.isPresent()).isTrue();
    Assertions.assertThat(output.get().id()).isEqualTo(expectedId);
    Assertions.assertThat(output.get().name()).isEqualTo(expectedName);
    Assertions.assertThat(output.get().email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.get().cnpj()).isEqualTo(expectedCnpj);
  }

  @Test
  @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
  void testGetByIdWithInvalidId() {
    final var expectedId = UUID.randomUUID().getMostSignificantBits();

    final var output = this.useCase.execute(new GetPartnerByIdUseCase.Input(expectedId));

    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
