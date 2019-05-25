package e2e;

import e2e.helpers.Client;
import org.eproject.protocol.GroupCreated;
import org.eproject.protocol.GroupState;
import org.eproject.protocol.Subscribed;
import org.eproject.protocol.core.ProtocolSerializable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeoutTest {
  private static final String HOST = "127.0.0.1";
  private static final int PORT = 4321;

  private static final byte[] PROFILE_PICTURE = { 0x01, 0x02 };
  private static final String GROUP_NAME = "group";

  private String invitationKey;
  private org.eproject.server.GeoServer server;

  @BeforeEach
  void beforeEach() throws Exception {
    server = new org.eproject.server.GeoServer();
    // Create server with 5 seconds idle timeout
    server.start(PORT, 5, null, null);
    // As there is no direct access to group db, group must be created by sending message to the server
    Client client = new Client(HOST, PORT);
    ProtocolSerializable r = client.sendCreateGroup(GROUP_NAME);
    invitationKey = ((GroupCreated) r).getInvitationKey();
  }

  @AfterEach
  void afterEach() {
    server.stop();
  }

  @Test
  void shouldKickUserOnTimeout() throws Exception {
    // Both users connects to the server
    Client clientA = new Client(HOST, PORT);
    int clientAId = ((Subscribed) clientA.sendSubscribe(invitationKey, "A", PROFILE_PICTURE)).getUserId();

    Client clientB = new Client(HOST, PORT);
    int clientBId = ((Subscribed) clientB.sendSubscribe(invitationKey, "B", PROFILE_PICTURE)).getUserId();

    // Both users must be aware of each other
    GroupState clientAGroupState = (GroupState) clientA.sendShareLocation(0, 0);
    GroupState clientBGroupState = (GroupState) clientB.sendShareLocation(0, 0);

    assertEquals(2, clientAGroupState.getLocations().size());
    if (clientAGroupState.getLocations().get(0).getMemberId() == clientAId) {
      assertEquals(clientBId, clientAGroupState.getLocations().get(1).getMemberId());
    } else {
      assertEquals(clientAId, clientAGroupState.getLocations().get(1).getMemberId());
    }

    assertEquals(2, clientBGroupState.getLocations().size());
    if (clientBGroupState.getLocations().get(0).getMemberId() == clientAId) {
      assertEquals(clientBId, clientBGroupState.getLocations().get(1).getMemberId());
    } else {
      assertEquals(clientAId, clientBGroupState.getLocations().get(1).getMemberId());
    }

    // Sleep 2 seconds
    Thread.sleep(4000);

    // User A sends message, so that server will not kick him
    clientA.sendShareLocation(0, 0);

    Thread.sleep(3000);

    // User B must be kicked from the group and connection must be closed
    clientAGroupState = (GroupState) clientA.sendShareLocation(0, 0);
    assertEquals(1, clientAGroupState.getLocations().size());
    assertEquals(clientAId, clientAGroupState.getLocations().get(0).getMemberId());
  }
}
