package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.Entities;

public record Cpf(String value) {

  public Cpf {
    Entities.notNull(value, "Customer cpf cannot be null");
    Entities.state(!value.isBlank(), "Customer cpf cannot be blank");
    Entities.state(value.matches("^\\d{3}.\\d{3}.\\d{3}-\\d{2}$"), "Invalid value for cpf");
  }

}
