package br.com.fullcycle.domain;

import br.com.fullcycle.domain.exceptions.ValidationException;

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

  public static void state(final boolean expression, final String message) {
    if (!expression) {
      throw new IllegalStateException(message);
    }
  }

  public static void notNull(final Object object, final String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

}
