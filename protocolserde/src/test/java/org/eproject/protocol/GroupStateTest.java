package org.eproject.protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.eproject.protocol.helpers.InputBuffer;
import org.junit.jupiter.api.Test;


public class GroupStateTest {
  @Test
  public void shouldSerDe() {
    byte[] picture = { 0x01, 0x02 };
    UserProfile user1 = new UserProfile();
    UserProfile user2 = new UserProfile();

    user1.setUserId(1);
    user1.setPicture(picture);
    user1.setUserName("user1");

    user2.setUserId(2);
    user2.setPicture(picture);
    user2.setUserName("user2");

    GroupState input = new GroupState();
    input.addNewMember(user1);
    input.addNewMember(user2);
    input.addMemberLocation(new MemberLocation(1, 0.1, 0.1));
    input.addMemberLocation(new MemberLocation(2, 0.5, 0.5));
    input.addMemberLocation(new MemberLocation(3, 0.9, 0.9));
    byte[] serialized = input.serialize();

    GroupState output = new GroupState();

    output.deserialize(new InputBuffer(serialized));

    assertEquals(2, output.getNewMembers().size());
    assertEquals(3, output.getLocations().size());
    assertEquals(1, output.getNewMembers().get(0).getUserId());
    assertEquals("user1", output.getNewMembers().get(0).getUserName());
    assertEquals(2, output.getNewMembers().get(1).getUserId());
    assertEquals("user2", output.getNewMembers().get(1).getUserName());

    assertEquals(1, output.getLocations().get(0).getMemberId());
    assertEquals(2, output.getLocations().get(1).getMemberId());
    assertEquals(3, output.getLocations().get(2).getMemberId());
  }
}
