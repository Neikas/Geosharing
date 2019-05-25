package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

final public class UserProfile implements ProtocolSerializable {
  private int userId;
  private String userName;
  private byte[] picture;

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public byte[] getPicture() {
    return picture;
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeInt(getUserId());
    buf.writeString(getUserName());
    buf.writeBytes(getPicture());

    return buf.getBytes();
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setUserId(buffer.getInt());
    setUserName(buffer.getString());
    setPicture(buffer.getBinary());
  }
}
