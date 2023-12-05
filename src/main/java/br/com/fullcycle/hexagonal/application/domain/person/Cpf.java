package br.com.fullcycle.hexagonal.application.domain.person;

import org.springframework.util.Assert;

public record Cpf(String value) {

  public Cpf {
    Assert.notNull(value, "Customer cpf cannot be null");
    Assert.state(!value.isBlank(), "Customer cpf cannot be blank");
    Assert.state(value.matches("^\\d{3}.\\d{3}.\\d{3}-\\d{2}$"), "Invalid value for cpf");
  }

}
