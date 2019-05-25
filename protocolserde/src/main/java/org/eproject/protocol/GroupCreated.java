package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

/**
 * Body of org.eproject.protocol.GroupCreated message
 * Successful reply on org.eproject.protocol.CreateGroup message
 *
 * Structure:
 *  - invitation key: string
 */
final public class GroupCreated implements ProtocolSerializable {
  private String invitationKey;

  public String getInvitationKey() {
    return invitationKey;
  }

  public void setInvitationKey(String invitationKey) {
    this.invitationKey = invitationKey;
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setInvitationKey(buffer.getString());
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeString(getInvitationKey());

    return buf.getBytes();
  }
}
