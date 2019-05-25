package org.eproject.protocol.core;

/**
 * A stateful byte buffer that implements write operations on primitive protocol types.
 */
public interface WritableBuffer {
  void writeByte(byte data);
  void writeUByte(int data);
  void writeShort(short data);
  void writeInt(int data);
  void writeDouble(double data);
  void writeString(String data);
  void writeBytes(byte[] data);
  void Opcode(Opcode data);

  void append(byte[] data);

  byte[] getBytes();
}
