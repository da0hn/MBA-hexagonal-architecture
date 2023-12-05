package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerTest {

  @Test
  @DisplayName("Deve instanciar um cliente")
  void test1() {
    final var expectedCpf = "123.456.789-00";
    final var expectedName = "Fulano";
    final var expectedEmail = "fulano@gmail.com";
    final var customer = Customer.newCustomer(expectedName, expectedCpf, expectedEmail);

    Assertions.assertThat(customer).isNotNull();
    Assertions.assertThat(customer.customerId()).isNotNull();
    Assertions.assertThat(customer.cpf().value()).isEqualTo(expectedCpf);
    Assertions.assertThat(customer.name().value()).isEqualTo(expectedName);
    Assertions.assertThat(customer.email().value()).isEqualTo(expectedEmail);
  }

  @Test
  @DisplayName("Não deve instanciar um cliente com cpf inválido")
  void test2() {
    final var expectedCpf = "123456789";
    final var expectedName = "Fulano";
    final var expectedEmail = "fulano@gmail.com";

    Assertions.assertThatThrownBy(() -> Customer.newCustomer(expectedName, expectedCpf, expectedEmail))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Invalid value for cpf");
  }

  @Test
  @DisplayName("Não deve instanciar um cliente com nome inválido")
  void test3() {
    final var expectedCpf = "123.456.789-00";
    final var expectedEmail = "fulano@gmail.com";
    final var expectedName = "  ";

    Assertions.assertThatThrownBy(() -> Customer.newCustomer(expectedName, expectedCpf, expectedEmail))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Name value cannot be blank");
  }

  @Test
  @DisplayName("Não deve instanciar um cliente com email inválido")
  void test4() {
    final var expectedName = "fulano";
    final var expectedCpf = "123.456.789-00";
    final var expectedEmail = "agmail.com";

    Assertions.assertThatThrownBy(() -> Customer.newCustomer(expectedName, expectedCpf, expectedEmail))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Invalid value for email");
  }

}
