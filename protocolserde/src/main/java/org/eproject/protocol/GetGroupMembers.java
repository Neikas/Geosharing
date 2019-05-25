package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

final public class GetGroupMembers implements ProtocolSerializable {
  @Override
  public byte[] serialize() {
    return new byte[0];
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
  }
}
