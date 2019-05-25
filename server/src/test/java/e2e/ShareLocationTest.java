package e2e;

import static org.junit.jupiter.api.Assertions.*;

import e2e.helpers.Client;
import org.eproject.protocol.core.ProtocolSerializable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.eproject.server.GeoServer;

import org.eproject.protocol.*;

import java.util.List;
import java.util.stream.Collectors;

class ShareLocationTest {
  private static final String HOST = "127.0.0.1";
  private static final int PORT = 4321;

  private GeoServer server;

  @BeforeEach
  void beforeEach() throws Exception {
    server = new GeoServer();
    server.start(PORT);
  }

  @AfterEach
  void afterEach() {
    server.stop();
  }

  @Test
  void shouldResponseWithGroupNotJoinedError_onGetGroupMembersWhenClientNotJoinAnyGroup() throws Exception {
    Client client = new Client(HOST, PORT);

    ProtocolSerializable response = client.sendShareLocation(0.1, 0.1);

    assertTrue(response instanceof org.eproject.protocol.Error);
    assertEquals(0x05, ((org.eproject.protocol.Error) response).getErrorCode());
  }

  @Test
  void shouldShareAllMembersLocations() throws Exception {
    byte[] picture = { 0x01 };
    Client client1 = new Client(HOST, PORT);
    Client client2 = new Client(HOST, PORT);

    String invitationKey = ((GroupCreated) client1.sendCreateGroup("group")).getInvitationKey();
    int user1 = ((Subscribed) client1.sendSubscribe(invitationKey, "user1", picture)).getUserId();

    List<UserProfile> profiles = ((GroupState) client1.sendShareLocation(1.23, 4.56)).getNewMembers();
    // only user1 joined the group, no new members should be returned
    assertEquals(0, profiles.size());

    int user2 = ((Subscribed) client2.sendSubscribe(invitationKey, "user2", picture)).getUserId();

    // user1 shares location and must receive own and user2 locations
    GroupState user1State = (GroupState) client1.sendShareLocation(1.23, 4.56);
    List<MemberLocation> ml = user1State.getLocations();
    List<UserProfile> nm = user1State.getNewMembers();

    // now, user1 should return the user2 profile
    assertEquals(1, nm.size());
    assertEquals(user2, nm.get(0).getUserId());

    assertEquals(2, ml.size());
    MemberLocation user1Location = ml.stream()
            .filter((u) -> u.getMemberId() == user1).collect(Collectors.toList()).get(0);
    MemberLocation user2Location = ml.stream()
            .filter((u) -> u.getMemberId() == user2).collect(Collectors.toList()).get(0);

    // user1 location
    assertEquals(1.23, user1Location.getLatitude());
    assertEquals(4.56, user1Location.getLongitude());
    // user2 location is not set yet
    assertEquals(0, user2Location.getLatitude());
    assertEquals(0, user2Location.getLongitude());

    // update user2 location
    GroupState user2State = (GroupState) client2.sendShareLocation(5.12, 6.78);

    // user2 should receive user 1 profile
    List<UserProfile> nm2 = user2State.getNewMembers();
    assertEquals(1, nm2.size());
    assertEquals(user1, nm2.get(0).getUserId());

    List<MemberLocation> ml2 = user2State.getLocations();

    user1Location = ml2.stream()
            .filter((u) -> u.getMemberId() == user1).collect(Collectors.toList()).get(0);
    user2Location = ml2.stream()
            .filter((u) -> u.getMemberId() == user2).collect(Collectors.toList()).get(0);

    assertEquals(1.23, user1Location.getLatitude());
    assertEquals(4.56, user1Location.getLongitude());
    // user2 location is not set yet
    assertEquals(5.12, user2Location.getLatitude());
    assertEquals(6.78, user2Location.getLongitude());
  }
}
