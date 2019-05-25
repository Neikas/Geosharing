package org.eproject.protocol.exceptions;

public class GroupNotJoined extends RuntimeException {
  public GroupNotJoined() {
    super();
  }

  public GroupNotJoined(String message) {
    super(message);
  }
}
