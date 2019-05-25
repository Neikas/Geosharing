package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

/**
 * Body of subscribed message
 *
 * Successful reply on org.eproject.protocol.Subscribe
 *
 * Structure:
 *  - user id: int
 */
final public class Subscribed implements ProtocolSerializable {
  private int userId;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setUserId(buffer.getInt());
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeInt(getUserId());

    return buf.getBytes();
  }
}
