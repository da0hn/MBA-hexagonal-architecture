package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.models.Partner;
import br.com.fullcycle.hexagonal.services.PartnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

class GetPartnerByIdUseCaseTest {

  @Test
  @DisplayName("Deve obter um parceiro por id")
  void testGetById() {
    final var expectedId = UUID.randomUUID().getMostSignificantBits();
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedCnpj = "12345678900";

    final var partnerService = Mockito.mock(PartnerService.class);
    final var aPartner = new Partner(expectedId, expectedName, expectedCnpj, expectedEmail);
    Mockito.doReturn(Optional.of(aPartner))
      .when(partnerService)
      .findById(expectedId);

    final var useCase = new GetPartnerByIdUseCase(partnerService);
    final var output = useCase.execute(new GetPartnerByIdUseCase.Input(expectedId));

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

    final var partnerService = Mockito.mock(PartnerService.class);
    Mockito.doReturn(Optional.empty())
      .when(partnerService)
      .findById(expectedId);

    final var useCase = new GetPartnerByIdUseCase(partnerService);
    final var output = useCase.execute(new GetPartnerByIdUseCase.Input(expectedId));

    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
