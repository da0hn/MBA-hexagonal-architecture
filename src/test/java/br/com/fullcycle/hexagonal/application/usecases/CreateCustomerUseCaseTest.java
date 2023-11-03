package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class CreateCustomerUseCaseTest {

  @Test
  @DisplayName("Deve criar um cliente")
  void testCreate() {
    final var expectedCPF = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var customerService = Mockito.mock(CustomerService.class);
    Mockito.doReturn(Optional.empty())
      .when(customerService)
      .findByCpf(expectedCPF);
    Mockito.doReturn(Optional.empty())
      .when(customerService)
      .findByEmail(expectedEmail);
    Mockito.doAnswer(a -> {
        final var customer = a.getArgument(0, Customer.class);
        customer.setId(UUID.randomUUID().getMostSignificantBits());
        return customer;
      })
      .when(customerService)
      .save(Mockito.any());
    final var useCase = new CreateCustomerUseCase(customerService);
    final var output = useCase.execute(input);

    Assertions.assertThat(output.id()).isNotNull();
    Assertions.assertThat(output.cpf()).isEqualTo(expectedCPF);
    Assertions.assertThat(output.email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.name()).isEqualTo(expectedName);
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  void testCreateWithDuplicatedCPFShouldFail() {
    final var expectedCPF = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = new Customer();
    aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
    aCustomer.setCpf(expectedCPF);
    aCustomer.setEmail(expectedEmail);
    aCustomer.setName(expectedName);

    final var customerService = Mockito.mock(CustomerService.class);
    Mockito.doReturn(Optional.of(aCustomer))
      .when(customerService)
      .findByCpf(expectedCPF);

    final var useCase = new CreateCustomerUseCase(customerService);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var expectedCPF = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = new Customer();
    aCustomer.setId(UUID.randomUUID().getMostSignificantBits());
    aCustomer.setCpf(expectedCPF);
    aCustomer.setEmail(expectedEmail);
    aCustomer.setName(expectedName);

    final var customerService = Mockito.mock(CustomerService.class);
    Mockito.doReturn(Optional.empty())
      .when(customerService)
      .findByCpf(expectedCPF);
    Mockito.doReturn(Optional.of(aCustomer))
      .when(customerService)
      .findByEmail(expectedEmail);

    final var useCase = new CreateCustomerUseCase(customerService);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

}
