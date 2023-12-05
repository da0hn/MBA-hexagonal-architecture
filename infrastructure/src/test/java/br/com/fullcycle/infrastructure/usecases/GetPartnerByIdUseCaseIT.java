package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.infrastructure.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    final var expectedCnpj = "12.345.678/0009-00";

    final var aPartner = this.partnerRepository.create(Partner.newPartner(expectedName, expectedCnpj, expectedEmail));

    final var expectedId = aPartner.partnerId().asString();

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
    final var output = this.useCase.execute(new GetPartnerByIdUseCase.Input(PartnerId.unique().asString()));
    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
