package org.eproject.protocol;

import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;

/**
 * Body of org.eproject.protocol.Welcome message
 *
 * Send by server in order to inform that the client version is supported by the server
 *
 * Structure:
 *  n/a
 */
final public class Welcome implements ProtocolSerializable {

  @Override
  public void deserialize(ReadableBuffer buffer) {
  }

  @Override
  public byte[] serialize() {
    return new byte[0];
  }
}
