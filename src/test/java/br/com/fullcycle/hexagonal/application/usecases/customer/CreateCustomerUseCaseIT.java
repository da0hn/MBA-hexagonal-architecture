package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
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
  private CustomerJpaRepository customerRepository;

  private static CustomerEntity createCustomer(final String cpf, final String email, final String name, final long customerId) {
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
    final var expectedCPF = "12345678901";
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
    final var expectedCPF = "12345678901";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe123@gmail.com";
    final var customerId = UUID.randomUUID().getMostSignificantBits();

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = createCustomer(expectedCPF, expectedEmail, expectedName, customerId);

    this.customerRepository.save(aCustomer);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  void testCreateWithDuplicatedEmailShouldFail() {
    final var existentCustomerCPF = "12345678901";
    final var expectedCPF = "00000000000";
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final var customerId = UUID.randomUUID().getMostSignificantBits();

    final var input = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var aCustomer = createCustomer(existentCustomerCPF, expectedEmail, expectedName, customerId);

    this.customerRepository.save(aCustomer);

    Assertions.assertThatThrownBy(() -> this.useCase.execute(input))
      .isInstanceOf(ValidationException.class)
      .hasMessage("Customer already exists");
  }

}
