package br.com.fullcycle.domain;

import br.com.fullcycle.domain.partner.Partner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PartnerTest {

  @Test
  @DisplayName("Deve instanciar um parceiro")
  void test1() {
    final var expectedCnpj = "12.345.678/0009-01";
    final var expectedName = "Fulano";
    final var expectedEmail = "fulano@gmail.com";
    final var partner = Partner.newPartner(expectedName, expectedCnpj, expectedEmail);

    Assertions.assertThat(partner).isNotNull();
    Assertions.assertThat(partner.partnerId()).isNotNull();
    Assertions.assertThat(partner.cnpj().value()).isEqualTo(expectedCnpj);
    Assertions.assertThat(partner.name().value()).isEqualTo(expectedName);
    Assertions.assertThat(partner.email().value()).isEqualTo(expectedEmail);
  }

  @Test
  @DisplayName("Não deve instanciar um parceiro com cnpj inválido")
  void test2() {
    final var expectedCnpj = "123456789";
    final var expectedName = "Fulano";
    final var expectedEmail = "fulano@gmail.com";

    Assertions.assertThatThrownBy(() -> Partner.newPartner(expectedName, expectedCnpj, expectedEmail))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Invalid value for cnpj");
  }

  @Test
  @DisplayName("Não deve instanciar um parceiro com nome inválido")
  void test3() {
    final var expectedCnpj = "12.345.678/0009-01";
    final var expectedEmail = "fulano@gmail.com";
    final var expectedName = "  ";

    Assertions.assertThatThrownBy(() -> Partner.newPartner(expectedName, expectedCnpj, expectedEmail))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Name value cannot be blank");
  }

  @Test
  @DisplayName("Não deve instanciar um parceiro com email inválido")
  void test4() {
    final var expectedName = "fulano";
    final var expectedCnpj = "12.345.678/0009-01";
    final var expectedEmail = "agmail.com";

    Assertions.assertThatThrownBy(() -> Partner.newPartner(expectedName, expectedCnpj, expectedEmail))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Invalid value for email");
  }

}
