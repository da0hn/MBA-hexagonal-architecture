package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetCustomerByIdUseCaseIT extends IntegrationTest {

  @Autowired
  private GetCustomerByIdUseCase useCase;

  @Autowired
  private CustomerJpaRepository customerRepository;

  @AfterEach
  void tearDown() {
    this.customerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve obter um cliente por id")
  void testGetById() {
    //    final var expectedName = "John Doe";
    //    final var expectedEmail = "john.doe@gmail.com";
    //    final var expectedCpf = "12345678900";
    //
    //    final var aCustomer = new Customer(null, expectedName, expectedCpf, expectedEmail);
    //
    //    this.customerRepository.save(aCustomer);
    //    final var expectedId = aCustomer.getId();
    //
    //    final var output = this.useCase.execute(new GetCustomerByIdUseCase.Input(expectedId));
    //
    //    Assertions.assertThat(output.isPresent()).isTrue();
    //    Assertions.assertThat(output.get().id()).isEqualTo(expectedId);
    //    Assertions.assertThat(output.get().name()).isEqualTo(expectedName);
    //    Assertions.assertThat(output.get().email()).isEqualTo(expectedEmail);
    //    Assertions.assertThat(output.get().cpf()).isEqualTo(expectedCpf);
  }

  @Test
  @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
  void testGetByIdWithInvalidId() {
    //    final var expectedId = UUID.randomUUID().getMostSignificantBits();
    //
    //    final var output = this.useCase.execute(new GetCustomerByIdUseCase.Input(expectedId));
    //
    //    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
