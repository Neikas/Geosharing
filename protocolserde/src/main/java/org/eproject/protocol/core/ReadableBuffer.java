package org.eproject.protocol.core;

/**
 * A stateful byte buffer that implements read operations on primitive protocol types.
 */
public interface ReadableBuffer {
  byte getByte();
  int getUByte();
  short getShort();
  int getInt();
  double getDouble();
  String getString();
  byte[] getBinary();
  Opcode getOpcode();
}
