package org.eproject.protocol.helpers;

import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.exceptions.InvalidBufferSize;
import org.eproject.protocol.exceptions.InvalidData;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Simple wrapper of ByteBuffer that implements
 * read functionality of protocol primitive types
 */

final public class InputBuffer implements ReadableBuffer {
  private ByteBuffer buffer;

  public InputBuffer(byte[] array) {
    this.buffer = ByteBuffer.wrap(array);
  }

  @Override
  public byte getByte() {
    try {
      return buffer.get();
    } catch (BufferUnderflowException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public int getUByte() {
    try {
      return buffer.get() & 0xFF;
    } catch (BufferUnderflowException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public short getShort() {
    try {
      return buffer.getShort();
    } catch (BufferUnderflowException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public int getInt() {
    try {
      return buffer.getInt();
    } catch (BufferUnderflowException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public double getDouble() {
    try {
      return buffer.getDouble();
    } catch (BufferUnderflowException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public String getString() {
    try {
      int stringLength = getUByte();

      byte[] byteBuf = new byte[stringLength];
      buffer.get(byteBuf, 0, stringLength);

      return new String(byteBuf);
    } catch (BufferUnderflowException | IndexOutOfBoundsException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public byte[] getBinary() {
    try {
      int binLength = getInt();

      byte[] byteBuf = new byte[binLength];
      buffer.get(byteBuf, 0, binLength);

      return byteBuf;
    } catch (BufferUnderflowException | IndexOutOfBoundsException e) {
      throw new InvalidBufferSize();
    }
  }

  @Override
  public Opcode getOpcode() {
    // Read and validate opcode
    int opcode = getUByte();

    if (opcode >= Opcode.values().length || opcode < 0) {
      throw new InvalidData(String.format("Corrupted opcode 0x%x", opcode));
    }

    return Opcode.values()[opcode];
  }
}
