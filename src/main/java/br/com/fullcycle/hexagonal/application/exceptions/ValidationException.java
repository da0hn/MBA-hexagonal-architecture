package br.com.fullcycle.hexagonal.application.exceptions;

import java.io.Serial;

public class ValidationException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -6561150215975495373L;

  public ValidationException(final String message) {
    super(message, null, true, false);
  }

  public ValidationException(final String message, final Throwable cause) {
    super(message, cause, true, false);
  }

}
