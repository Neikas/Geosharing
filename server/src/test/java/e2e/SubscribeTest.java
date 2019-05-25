package e2e;

import static org.junit.jupiter.api.Assertions.*;

import e2e.helpers.Client;
import org.eproject.protocol.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.eproject.server.GeoServer;

import org.eproject.protocol.core.ProtocolSerializable;

class SubscribeTest {
  private static final String HOST = "127.0.0.1";
  private static final int PORT = 4321;

  private static final byte[] PROFILE_PICTURE = { 0x01, 0x02 };
  private static final String GROUP_NAME = "group";

  private String invitationKey;
  private GeoServer server;

  @BeforeEach
  void beforeEach() throws Exception {
    server = new GeoServer();
    server.start(PORT);
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
  void shouldResponseWithGroupNotFound_onSubscribeGroupWithInvalidSecret() throws Exception {
    Client client = new Client(HOST, PORT);

    // Current, no groups are created, therefore any group name can be used
    ProtocolSerializable response = client.sendSubscribe("somename:secret", "user", PROFILE_PICTURE);

    assertTrue(response instanceof org.eproject.protocol.Error);
    assertEquals(0x04, ((org.eproject.protocol.Error) response).getErrorCode());
  }

  @Test
  void shouldResponseWithGroupNotFound_onSubscribeGroupWithExistingNameAndInvalidSecret() throws Exception {
    Client client = new Client(HOST, PORT);

    // Current, no groups are created, therefore any group name can be used
    ProtocolSerializable response = client.sendSubscribe(String.format("%s:secret123", GROUP_NAME), "user", PROFILE_PICTURE);

    assertTrue(response instanceof org.eproject.protocol.Error);
    assertEquals(0x04, ((org.eproject.protocol.Error) response).getErrorCode());
  }

  @Test
  void shouldResponseWithSubscribed() throws Exception {
    Client client = new Client(HOST, PORT);

    // Current, no groups are created, therefore any group name can be used
    ProtocolSerializable response = client.sendSubscribe(String.format(invitationKey, GROUP_NAME), "user", PROFILE_PICTURE);

    assertTrue(response instanceof Subscribed);
  }
}
