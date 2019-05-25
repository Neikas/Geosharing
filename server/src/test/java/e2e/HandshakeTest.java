package e2e;

import static org.junit.jupiter.api.Assertions.*;

import e2e.helpers.Client;
import org.eproject.protocol.Welcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.eproject.server.GeoServer;

import  org.eproject.protocol.core.ProtocolSerializable;

class HandshakeTest {
  private static final String HOST = "127.0.0.1";
  private static final int PORT = 4321;
  private static final short PROTOCOL_VERSION = 0x1;

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
  void shouldRespondWithWelcome_onHelloIs() throws Exception {
    Client client = new Client(HOST, PORT);
    ProtocolSerializable response = client.sendHello((short)PROTOCOL_VERSION);

    assertTrue(response instanceof Welcome);
  }
  
  @Test
  void shouldResponsWithInvalidProtocolVersion_onHelloWithUnsupportedVersion() throws Exception {
    Client client = new Client(HOST, PORT);
    ProtocolSerializable response = client.sendHello((short)0);

    assertTrue(response instanceof org.eproject.protocol.Error);
    assertEquals(((org.eproject.protocol.Error) response).getErrorCode(), 0x03);
  }
}
