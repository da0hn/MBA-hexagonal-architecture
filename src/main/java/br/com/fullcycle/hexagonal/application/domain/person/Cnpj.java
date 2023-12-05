package br.com.fullcycle.hexagonal.application.domain.person;

import org.springframework.util.Assert;

public record Cnpj(String value) {

  public Cnpj {
    Assert.notNull(value, "Cnpj cannot be null");
    Assert.state(!value.isBlank(), "Cnpj cannot be blank");
    Assert.state(value.matches("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$"), "Invalid value for cnpj");
  }

}
