package e2e;

import static org.junit.jupiter.api.Assertions.*;

import e2e.helpers.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.eproject.server.GeoServer;

import org.apache.commons.lang.StringUtils;

import org.eproject.protocol.*;
import org.eproject.protocol.core.ProtocolSerializable;

class CreateGroupTest {
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
  void shouldResponseWithInvalidDataError_onCreateGroupWithEmptyName() throws Exception {
    Client client = new Client(HOST, PORT);

    ProtocolSerializable response = client.sendCreateGroup("");

    assertTrue(response instanceof org.eproject.protocol.Error);
    assertEquals(((org.eproject.protocol.Error) response).getErrorCode(), 0x02);
  }

  @Test
  void shouldResponseWithInvalidDataError_onCreateGroupWithLongName() throws Exception {
    Client client = new Client(HOST, PORT);

    ProtocolSerializable response = client.sendCreateGroup(StringUtils.repeat("a", 33));

    assertTrue(response instanceof org.eproject.protocol.Error);
    assertEquals(((org.eproject.protocol.Error) response).getErrorCode(), 0x02);
  }

  @Test
  void shouldResponseWithInvitationKey() throws Exception {
    Client client = new Client(HOST, PORT);

    ProtocolSerializable response = client.sendCreateGroup("group");

    assertTrue(response instanceof GroupCreated);

    GroupCreated r = (GroupCreated) response;
    assertTrue(r.getInvitationKey().startsWith("group:"));
  }
}
