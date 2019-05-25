package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

final public class Subscribe implements ProtocolSerializable {
  private String invitationKey;
  private String userName;
  private byte[] imageData;


  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeString(getInvitationKey());
    buf.writeString(getUserName());
    buf.writeBytes(imageData);

    return buf.getBytes();
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setInvitationKey(buffer.getString());
    setUserName(buffer.getString());
    setImageData(buffer.getBinary());
  }

  public String getInvitationKey() {
    return invitationKey;
  }

  public void setInvitationKey(String invitationKey) {
    this.invitationKey = invitationKey;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public byte[] getImageData() {
    return imageData;
  }

  public void setImageData(byte[] imageData) {
    this.imageData = imageData;
  }
}
