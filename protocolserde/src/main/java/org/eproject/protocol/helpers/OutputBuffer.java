package org.eproject.protocol.helpers;

import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.core.WritableBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class OutputBuffer implements WritableBuffer {
  private ByteArrayOutputStream buffer;

  public OutputBuffer() {
    buffer = new ByteArrayOutputStream();
  }

  @Override
  public void writeByte(byte data) {
    buffer.write(data);
  }

  @Override
  public void writeUByte(int data) {
    buffer.write((byte)data);
  }

  @Override
  public void writeShort(short data) {
    byte[] array = new byte[2];
    ByteBuffer.wrap(array).putShort(data);

    try {
      buffer.write(array);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeInt(int data) {
    byte[] array = new byte[4];
    ByteBuffer.wrap(array).putInt(data);

    try {
      buffer.write(array);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeDouble(double data) {
    byte[] array = new byte[8];
    ByteBuffer.wrap(array).putDouble(data);

    try {
      buffer.write(array);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeString(String data) {
    writeUByte(data.length());
    try {
      buffer.write(data.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeBytes(byte[] data) {
    writeInt(data.length);
    try {
      buffer.write(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void Opcode(Opcode data) {

  }

  @Override
  public void append(byte[] data) {
    try {
      buffer.write(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] getBytes() {
    return buffer.toByteArray();
  }
}
