package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.Entities;

public record Name(String value) {

  public Name {
    Entities.notNull(value, "Name value cannot be null");
    Entities.state(!value.isBlank(), "Name value cannot be blank");
  }

}
