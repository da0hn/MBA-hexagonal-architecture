package br.com.fullcycle.application.usecases.customer;

import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.exceptions.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateCustomerUseCaseTest {

  @Test
  @DisplayName("Deve criar um cliente")
  void testCreate() {
    final var expectedCPF = "123.456.789-01";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var customerRepository = new InMemoryCustomerRepository();
    final var useCase = new CreateCustomerUseCase(customerRepository);
    final var output = useCase.execute(input);

    Assertions.assertThat(output.id()).isNotNull();
    Assertions.assertThat(output.cpf()).isEqualTo(expectedCPF);
    Assertions.assertThat(output.email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.name()).isEqualTo(expectedName);
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  void testCreateWithDuplicatedCPFShouldFail() {
    final var expectedCPF = "123.456.789-01";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var customerRepository = new InMemoryCustomerRepository();

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, "john.doe2@gmail.com");
    customerRepository.create(aCustomer);

    final var useCase = new CreateCustomerUseCase(customerRepository);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var expectedCPF = "123.456.789-01";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var customerRepository = new InMemoryCustomerRepository();

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = Customer.newCustomer(expectedName, "123.456.789-02", expectedEmail);
    customerRepository.create(aCustomer);

    final var useCase = new CreateCustomerUseCase(customerRepository);
    Assertions.assertThatThrownBy(() -> useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

}
