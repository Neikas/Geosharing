package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

/**
 * Body of org.eproject.protocol.Hello message
 *
 * Send by the client in order to indicate the actual protocol version
 *
 * Structure:
 *  protocol version: short
 */
final public class Hello implements ProtocolSerializable {
  private short protocolVersion;

  public short getProtocolVersion() {
    return protocolVersion;
  }

  public void setProtocolVersion(short protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setProtocolVersion(buffer.getShort());
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeShort(getProtocolVersion());

    return buf.getBytes();
  }
}
