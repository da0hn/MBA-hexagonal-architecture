package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public final class Entities {

  private Entities() { }

  public static <T> T requireNonNull(final T value) {
    if (value == null) {
      throw new ValidationException("Value cannot be null");
    }
    return value;
  }

  public static <T> T requireNonNull(final T value, final String message) {
    if (value == null) {
      throw new ValidationException(message);
    }
    return value;
  }

}
