package org.eproject.protocol;

import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;
import org.eproject.protocol.helpers.OutputBuffer;

import java.util.List;

public class GroupState implements ProtocolSerializable {
  private ObjectList<UserProfile> newMembers = new ObjectList<>(UserProfile.class);
  private ObjectList<MemberLocation> locations = new ObjectList<>(MemberLocation.class);

  public void addNewMember(UserProfile profile) {
    newMembers.asList().add(profile);
  }

  public void addMemberLocation(MemberLocation location) {
    locations.asList().add(location);
  }

  public List<UserProfile> getNewMembers() {
    return newMembers.asList();
  }

  public List<MemberLocation> getLocations() {
    return locations.asList();
  }

  @Override
  public byte[] serialize() {
    byte[] membersBuf = newMembers.serialize();
    byte[] locationsBuf = locations.serialize();

    byte[] output = new byte[membersBuf.length + locationsBuf.length];

    System.arraycopy(membersBuf, 0, output, 0, membersBuf.length);
    System.arraycopy(locationsBuf, 0, output, membersBuf.length, locationsBuf.length);

    return output;
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    newMembers.deserialize(buffer);
    locations.deserialize(buffer);
  }
}
