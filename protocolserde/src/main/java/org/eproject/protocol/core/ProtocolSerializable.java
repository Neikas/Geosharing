package org.eproject.protocol.core;

public interface ProtocolSerializable {
  byte[] serialize();
  void deserialize(ReadableBuffer buffer);
}
