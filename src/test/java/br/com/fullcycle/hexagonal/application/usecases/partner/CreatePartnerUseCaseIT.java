package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreatePartnerUseCaseIT extends IntegrationTest {

  @Autowired
  private CreatePartnerUseCase useCase;

  @Autowired
  private PartnerJpaRepository partnerRepository;

  private PartnerEntity createPartner(final String cnpj, final String email, final String name) {
    final var aPartner = new PartnerEntity();
    aPartner.setCnpj(cnpj);
    aPartner.setEmail(email);
    aPartner.setName(name);
    return this.partnerRepository.save(aPartner);
  }

  @AfterEach
  void tearDown() {
    this.partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um parceiro")
  void testCreate() {
    final var expectedCNPJ = "12345678901";
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
    final var expectedCNPJ = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var aPartner = this.createPartner(expectedCNPJ, "john.doe123@gmail.com", "Another John Doe");

    final var input = new CreatePartnerUseCase.Input(aPartner.getCnpj(), expectedEmail, expectedName);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var expectedCNPJ = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var aPartner = this.createPartner("12312312300", expectedEmail, "Another John Doe");

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, aPartner.getEmail(), expectedName);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

}
