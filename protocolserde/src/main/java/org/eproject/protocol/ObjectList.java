package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;

import java.util.LinkedList;
import java.util.List;

final public class ObjectList<T extends ProtocolSerializable>
        implements ProtocolSerializable {
  private List<T> objects = new LinkedList<>();
  private Class<T> clazz;

  ObjectList(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    byte elements = buffer.getByte();

    try {
      for (int i = 0; i < elements; i++) {
        T obj = clazz.newInstance();
        obj.deserialize(buffer);
        objects.add(obj);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    // write list length
    buf.writeUByte(objects.size());

    for (ProtocolSerializable e : objects) {
      buf.append(e.serialize());
    }

    return buf.getBytes();
  }

  public List<T> asList() {
    return objects;
  }
}
