package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatePartnerUseCaseTest {

  @Test
  @DisplayName("Deve criar um parceiro")
  void testCreate() {
    final var expectedCNPJ = "12.345.678/0009-00";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var partnerRepository = new InMemoryPartnerRepository();
    final var useCase = new CreatePartnerUseCase(partnerRepository);
    final var output = useCase.execute(input);

    Assertions.assertThat(output.id()).isNotNull();
    Assertions.assertThat(output.cnpj()).isEqualTo(expectedCNPJ);
    Assertions.assertThat(output.email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.name()).isEqualTo(expectedName);
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CNPJ duplicado")
  void testCreateWithDuplicatedCPFShouldFail() {
    final var expectedCNPJ = "12.345.678/0009-00";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, "john.doe2@gmail.com");

    final var partnerRepository = new InMemoryPartnerRepository();
    partnerRepository.create(aPartner);

    final var useCase = new CreatePartnerUseCase(partnerRepository);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var expectedCNPJ = "12.345.678/0009-00";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var aPartner = Partner.newPartner(expectedName, "12.345.678/0009-01", expectedEmail);
    final var partnerRepository = new InMemoryPartnerRepository();
    partnerRepository.create(aPartner);

    final var useCase = new CreatePartnerUseCase(partnerRepository);

    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

}
