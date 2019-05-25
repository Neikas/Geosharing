package org.eproject.protocol.exceptions;

final public class InternalError extends RuntimeException {
  public InternalError() {
    super();
  }

  public InternalError(String message) {
    super(message);
  }
}
