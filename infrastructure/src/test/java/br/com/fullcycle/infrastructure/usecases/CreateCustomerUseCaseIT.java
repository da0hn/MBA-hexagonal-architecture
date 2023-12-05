package br.com.fullcycle.infrastructure.usecases;

import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.infrastructure.IntegrationTest;
import br.com.fullcycle.infrastructure.jpa.entities.CustomerEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class CreateCustomerUseCaseIT extends IntegrationTest {

  @Autowired
  private CreateCustomerUseCase useCase;

  @Autowired
  private CustomerRepository customerRepository;

  private static CustomerEntity createCustomer(final String cpf, final String email, final String name, final UUID customerId) {
    final var aCustomer = new CustomerEntity();
    aCustomer.setId(customerId);
    aCustomer.setCpf(cpf);
    aCustomer.setEmail(email);
    aCustomer.setName(name);
    return aCustomer;
  }

  @AfterEach
  void tearDown() {
    this.customerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um cliente")
  void testCreate() {
    final var expectedCPF = "123.456.789-01";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var output = this.useCase.execute(input);

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
    final var expectedEmail = "john.doe123@gmail.com";

    final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

    this.customerRepository.create(aCustomer);

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var existentCustomerCPF = "123.456.789-01";
    final var expectedCPF = "123.456.789-02";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final UUID customerId = UUID.randomUUID();

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = Customer.newCustomer(expectedName, existentCustomerCPF, expectedEmail);

    this.customerRepository.create(aCustomer);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

}
