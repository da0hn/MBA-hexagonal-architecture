package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

class GetCustomerByIdUseCaseTest {

  @Test
  @DisplayName("Deve obter um cliente por id")
  void testGetById() {
    final var expectedId = UUID.randomUUID().getMostSignificantBits();
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedCpf = "12345678900";

    final var customerService = Mockito.mock(CustomerService.class);
    final var aCustomer = new Customer(expectedId, expectedName, expectedCpf, expectedEmail);
    Mockito.doReturn(Optional.of(aCustomer))
      .when(customerService)
      .findById(expectedId);

    final var useCase = new GetCustomerByIdUseCase(customerService);
    final var output = useCase.execute(new GetCustomerByIdUseCase.Input(expectedId));

    Assertions.assertThat(output.isPresent()).isTrue();
    Assertions.assertThat(output.get().id()).isEqualTo(expectedId);
    Assertions.assertThat(output.get().name()).isEqualTo(expectedName);
    Assertions.assertThat(output.get().email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.get().cpf()).isEqualTo(expectedCpf);
  }

  @Test
  @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
  void testGetByIdWithInvalidId() {
    final var expectedId = UUID.randomUUID().getMostSignificantBits();

    final var customerService = Mockito.mock(CustomerService.class);
    Mockito.doReturn(Optional.empty())
      .when(customerService)
      .findById(expectedId);

    final var useCase = new GetCustomerByIdUseCase(customerService);
    final var output = useCase.execute(new GetCustomerByIdUseCase.Input(expectedId));

    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
