package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetPartnerByIdUseCaseTest {

  @Test
  @DisplayName("Deve obter um parceiro por id")
  void testGetById() {
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedCnpj = "12.345.678/0009-00";
    final var aPartner = Partner.newPartner(expectedName, expectedCnpj, expectedEmail);
    final var expectedId = aPartner.partnerId();

    final var partnerRepository = new InMemoryPartnerRepository();
    partnerRepository.create(aPartner);

    final var useCase = new GetPartnerByIdUseCase(partnerRepository);
    final var output = useCase.execute(new GetPartnerByIdUseCase.Input(expectedId.asString()));

    Assertions.assertThat(output.isPresent()).isTrue();
    Assertions.assertThat(output.get().id()).isEqualTo(expectedId.asString());
    Assertions.assertThat(output.get().name()).isEqualTo(expectedName);
    Assertions.assertThat(output.get().email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.get().cnpj()).isEqualTo(expectedCnpj);
  }

  @Test
  @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
  void testGetByIdWithInvalidId() {
    final var expectedId = PartnerId.unique();

    final var partnerRepository = new InMemoryPartnerRepository();

    final var useCase = new GetPartnerByIdUseCase(partnerRepository);
    final var output = useCase.execute(new GetPartnerByIdUseCase.Input(expectedId.asString()));

    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
