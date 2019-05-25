package org.eproject.protocol.exceptions;

final public class InvalidData extends RuntimeException {
  public InvalidData() {
    super();
  }

  public InvalidData(String message) {
    super(message);
  }
}
