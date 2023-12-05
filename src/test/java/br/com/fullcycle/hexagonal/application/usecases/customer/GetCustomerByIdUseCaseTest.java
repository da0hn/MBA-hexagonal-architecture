package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GetCustomerByIdUseCaseTest {

  @Test
  @DisplayName("Deve obter um cliente por id")
  void testGetById() {
    final var expectedId = CustomerId.unique();
    final var expectedName = "John Doe";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedCpf = "123.456.789-00";

    final var customerRepository = new InMemoryCustomerRepository();

    final var aCustomer = new Customer(expectedId, new Name(expectedName), new Cpf(expectedCpf), new Email(expectedEmail));
    customerRepository.create(aCustomer);

    final var useCase = new GetCustomerByIdUseCase(customerRepository);
    final var output = useCase.execute(new GetCustomerByIdUseCase.Input(expectedId.asString()));

    Assertions.assertThat(output.isPresent()).isTrue();
    Assertions.assertThat(output.get().id()).isEqualTo(expectedId.asString());
    Assertions.assertThat(output.get().name()).isEqualTo(expectedName);
    Assertions.assertThat(output.get().email()).isEqualTo(expectedEmail);
    Assertions.assertThat(output.get().cpf()).isEqualTo(expectedCpf);
  }

  @Test
  @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
  void testGetByIdWithInvalidId() {
    final var expectedId = CustomerId.unique();

    final var customerRepository = new InMemoryCustomerRepository();

    final var useCase = new GetCustomerByIdUseCase(customerRepository);
    final var output = useCase.execute(new GetCustomerByIdUseCase.Input(expectedId.asString()));

    Assertions.assertThat(output.isEmpty()).isTrue();
  }

}
