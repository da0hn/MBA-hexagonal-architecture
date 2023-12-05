package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.infrastructure.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreatePartnerUseCaseIT extends IntegrationTest {

  @Autowired
  private CreatePartnerUseCase useCase;

  @Autowired
  private PartnerRepository partnerRepository;

  private Partner createPartner(final String cnpj, final String email, final String name) {
    return this.partnerRepository.create(Partner.newPartner(name, cnpj, email));
  }

  @AfterEach
  void tearDown() {
    this.partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um parceiro")
  void testCreate() {
    final var expectedCNPJ = "12.345.678/9010-00";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var output = this.useCase.execute(input);

    Assertions.assertThat(output.id()).isNotNull();
    Assertions.assertThat(output.cnpj()).isEqualTo(expectedCNPJ);
    Assertions.assertThat(output.email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.name()).isEqualTo(expectedName);
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  void testCreateWithDuplicatedCPFShouldFail() {
    final var expectedCNPJ = "12.345.678/9010-00";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var aPartner = this.createPartner(expectedCNPJ, "john.doe123@gmail.com", "Another John Doe");

    final var input = new CreatePartnerUseCase.Input(aPartner.cnpj().value(), expectedEmail, expectedName);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var expectedCNPJ = "12.345.678/9010-00";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var aPartner = this.createPartner("12.312.312/3000-01", expectedEmail, "Another John Doe");

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, aPartner.email().value(), expectedName);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

}
