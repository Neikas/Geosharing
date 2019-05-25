package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

/**
 * Message header
 *
 * Structure:
 *  - opcode: byte
 *  - body length: int
 */
final public class Header implements ProtocolSerializable {
  private int payloadSize;
  private Opcode opcode;

  final public static byte HEADER_LENGTH = 0x5;

  public Opcode getOpcode() {
    return opcode;
  }

  public int getPayloadSize() {
    return payloadSize;
  }

  public void setOpcode(Opcode opcode) {
    this.opcode = opcode;
  }

  public void setPayloadSize(int payloadSize) {
    this.payloadSize = payloadSize;
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setOpcode(buffer.getOpcode());

    // TODO: consider to limit payload length to some mb's
    setPayloadSize(buffer.getInt());
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeUByte(getOpcode().ordinal());
    buf.writeInt(getPayloadSize());

    return buf.getBytes();
  }
}
