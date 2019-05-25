package org.eproject.protocol;

import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;
import org.eproject.protocol.helpers.OutputBuffer;

public class ShareLocation implements ProtocolSerializable {
  private double longitude;
  private double latitude;

  public void setLocation(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeDouble(getLongitude());
    buf.writeDouble(getLatitude());

    return buf.getBytes();
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    double lon = buffer.getDouble();
    double lat = buffer.getDouble();
    setLocation(lon, lat);
  }
}
