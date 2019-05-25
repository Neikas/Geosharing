package org.eproject.protocol;

import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;
import org.eproject.protocol.helpers.OutputBuffer;

import java.lang.reflect.Member;

public class MemberLocation implements ProtocolSerializable {
  int memberId;
  double lat;
  double lon;

  public MemberLocation() {}

  public MemberLocation(int memberId, double lat, double lon) {
    this.memberId = memberId;
    this.lat = lat;
    this.lon = lon;
  }

  public int getMemberId() {
    return memberId;
  }

  public double getLongitude() {
    return lon;
  }

  public double getLatitude() {
    return lat;
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeInt(getMemberId());
    buf.writeDouble(getLatitude());
    buf.writeDouble(getLongitude());

    return buf.getBytes();
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    memberId = buffer.getInt();
    lat = buffer.getDouble();
    lon = buffer.getDouble();
  }
}