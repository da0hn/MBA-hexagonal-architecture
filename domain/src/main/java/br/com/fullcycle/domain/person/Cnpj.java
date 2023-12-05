package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.Entities;

public record Cnpj(String value) {

  public Cnpj {
    Entities.notNull(value, "Cnpj cannot be null");
    Entities.state(!value.isBlank(), "Cnpj cannot be blank");
    Entities.state(value.matches("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$"), "Invalid value for cnpj");
  }

}
