package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

final public class CreateGroup implements ProtocolSerializable {
  private String groupName;

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }


  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeString(getGroupName());

    return buf.getBytes();
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setGroupName(buffer.getString());
  }
}
