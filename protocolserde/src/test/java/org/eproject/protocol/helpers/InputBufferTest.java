package org.eproject.protocol.helpers;

import org.eproject.protocol.exceptions.InvalidBufferSize;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class InputBufferTest {
  @Test
  void getByte() {
    byte[] array = { 0x7A };

    assertEquals(0x7A, new InputBuffer(array).getByte());
  }

  @Test
  void getByteShouldThrowInvalidBufferSizeExceptionWhenAmountOfDataIsInsufficient() {
    byte[] array = {};

    assertThrows(InvalidBufferSize.class, () -> {
      new InputBuffer(array).getByte();
    });
  }

  @Test
  void getShort() {
    byte[] array = { 0x1A, 0x2B };

    assertEquals(0x1A2B, new InputBuffer(array).getShort());
  }

  @Test
  void getShortShouldThrowInvalidBufferSizeExceptionWhenAmountOfDataIsInsufficient() {
    byte[] array = { 0x11 };

    assertThrows(InvalidBufferSize.class, () -> {
      new InputBuffer(array).getShort();
    });
  }

  @Test
  void getUByte() {
    byte[] array = { -22 };

    assertEquals(234, new InputBuffer(array).getUByte());
  }

  @Test
  void getUByteShouldThrowInvalidBufferSizeExceptionWhenAmountOfDataIsInsufficient() {
    byte[] array = {};

    assertThrows(InvalidBufferSize.class, () -> {
      new InputBuffer(array).getShort();
    });
  }

  @Test
  void getInt() {
    byte[] array = { 0x1B, 0x2B, 0x3B, 0x4B };

    assertEquals(0x1B2B3B4B, new InputBuffer(array).getInt());
  }

  @Test
  void getIntShouldThrowInvalidBufferSizeExceptionWhenAmountOfDataIsInsufficient() {
    byte[] array = { 0x1B };

    assertThrows(InvalidBufferSize.class, () -> {
      new InputBuffer(array).getInt();
    });
  }

  @Test
  void getDouble() {
    byte[] array = new byte[8];
    ByteBuffer.wrap(array).putDouble(1.234f);

    assertEquals(1.234f, new InputBuffer(array).getDouble());
  }

  @Test
  void getDoubleShouldThrowInvalidBufferSizeExceptionWhenAmountOfDataIsInsufficient() {
    byte[] array = { 0x1, 0x2 };

    assertThrows(InvalidBufferSize.class, () -> {
      new InputBuffer(array).getInt();
    });
  }

  @Test
  void getString() {
    byte[] array = { 0x3, 'a', 'b', 'c' };

    assertEquals("abc", new InputBuffer(array).getString());
  }

  @Test
  void getStringThrowsInvalidBufferSizeExceptionWhenPayloadLengthIsSmallerThanSpecified() {
    byte[] array = { 0x3, 'a', 'b' };

    assertThrows(InvalidBufferSize.class, () -> {
      new InputBuffer(array).getInt();
    });
  }
}
