package org.eproject.protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.junit.jupiter.api.Test;

class ObjectListTest {

  @Test
  void serializeShouldFinishSuccessfullyOnValidList() {
    // first byte - list length, remaining - elements
    byte[] array = { 0x02, 0x09, 0x5A, 0x6B, 0x7c };
    ObjectList<TestObject> list = new ObjectList<>(TestObject.class);

    list.deserialize(new Buffer(array));

    assertEquals(2, list.asList().size());
    assertEquals(0x09, list.asList().get(0).firstByte);
    assertEquals(0x5A, list.asList().get(0).secondByte);
    assertEquals(0x6B, list.asList().get(1).firstByte);
    assertEquals(0x7c, list.asList().get(1).secondByte);
  }
}

/**
 * Simple readable buffer that implements only getByte methods, as it is the only
 * method that is required by this set of cases.
 */
class Buffer implements ReadableBuffer {
  private byte[] array;
  private int offset;

  Buffer(byte[] array) {
    this.array = array;
    offset = 0;
  }

  @Override
  public byte getByte() {
    return array[offset++];
  }

  @Override
  public int getUByte() {
    throw new RuntimeException();
  }

  @Override
  public short getShort() {
    throw new RuntimeException();
  }

  @Override
  public int getInt() {
    throw new RuntimeException();
  }

  @Override
  public double getDouble() {
    throw new RuntimeException();
  }

  @Override
  public String getString() {
    throw new RuntimeException();
  }

  @Override
  public byte[] getBinary() {
    throw new RuntimeException();
  }

  @Override
  public Opcode getOpcode() {
    throw new RuntimeException();
  }
}

/**
 * Testable objects that consists of 2 bytes
 */
class TestObject implements ProtocolSerializable {
  byte firstByte;
  byte secondByte;

  @Override
  public byte[] serialize() {
    throw new RuntimeException();
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    firstByte = buffer.getByte();
    secondByte = buffer.getByte();
  }

}
