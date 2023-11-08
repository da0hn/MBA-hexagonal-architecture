package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

class CreatePartnerUseCaseTest {

  @Test
  @DisplayName("Deve criar um parceiro")
  void testCreate() {
    final var expectedCNPJ = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var partnerService = Mockito.mock(PartnerService.class);
    Mockito.doReturn(Optional.empty())
      .when(partnerService)
      .findByCnpj(expectedCNPJ);
    Mockito.doReturn(Optional.empty())
      .when(partnerService)
      .findByEmail(expectedEmail);
    Mockito.doAnswer(a -> {
        final var partner = a.getArgument(0, Partner.class);
        partner.setId(UUID.randomUUID().getMostSignificantBits());
        return partner;
      })
      .when(partnerService)
      .save(Mockito.any());
    final var useCase = new CreatePartnerUseCase(partnerService);
    final var output = useCase.execute(input);

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

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var aPartner = new Partner();
    aPartner.setId(UUID.randomUUID().getMostSignificantBits());
    aPartner.setCnpj(expectedCNPJ);
    aPartner.setEmail(expectedEmail);
    aPartner.setName(expectedName);

    final var partnerService = Mockito.mock(PartnerService.class);
    Mockito.doReturn(Optional.of(aPartner))
      .when(partnerService)
      .findByCnpj(expectedCNPJ);

    final var useCase = new CreatePartnerUseCase(partnerService);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var expectedCNPJ = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var aPartner = new Partner();
    aPartner.setId(UUID.randomUUID().getMostSignificantBits());
    aPartner.setCnpj(expectedCNPJ);
    aPartner.setEmail(expectedEmail);
    aPartner.setName(expectedName);

    final var partnerService = Mockito.mock(PartnerService.class);
    Mockito.doReturn(Optional.empty())
      .when(partnerService)
      .findByCnpj(expectedCNPJ);
    Mockito.doReturn(Optional.of(aPartner))
      .when(partnerService)
      .findByEmail(expectedEmail);

    final var useCase = new CreatePartnerUseCase(partnerService);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Partner already exists");
  }

}
