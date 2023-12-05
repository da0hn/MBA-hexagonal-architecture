package br.com.fullcycle.domain.person;

import br.com.fullcycle.domain.Entities;

public record Email(String value) {

  public Email {
    Entities.notNull(value, "Email cannot be null");
    Entities.state(!value.isBlank(), "Email cannot be empty");
    Entities.state(value.matches("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$"), "Invalid value for email");
  }

}
