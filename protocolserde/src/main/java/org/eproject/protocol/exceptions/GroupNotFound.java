package org.eproject.protocol.exceptions;

final public class GroupNotFound extends RuntimeException {
  public GroupNotFound() {
    super();
  }

  public GroupNotFound(String message) {
    super(message);
  }
}
