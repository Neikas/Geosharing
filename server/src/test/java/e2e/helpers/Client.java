package e2e.helpers;

import java.net.Socket;
import java.io.InputStream;

import org.eproject.protocol.*;
import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.helpers.InputBuffer;

/**
 * Simple implementation of a client used in e2e testing.
 */
public class Client {
  private Socket socket;

  public Client(String host, int port) throws Exception {
    this.socket = new Socket(host, port);
  }

  public void sendBytes(byte[] buffer) throws Exception {
    this.socket.getOutputStream().write(buffer);
  }

  public void sendMessage(Opcode type, ProtocolSerializable message) throws Exception {
    byte[] payload = message.serialize();
    Header header = new Header();
    header.setOpcode(type);
    header.setPayloadSize(payload.length);

    sendBytes(header.serialize());
    sendBytes(payload);
  }

  public ProtocolSerializable readResponse() throws Exception {
    InputStream is = this.socket.getInputStream();
    byte[] headerBuf = new byte[Header.HEADER_LENGTH];

    is.read(headerBuf);
    Header header = new Header();
    header.deserialize(new InputBuffer(headerBuf));

    byte[] payloadBuf = new byte[header.getPayloadSize()];
    is.read(payloadBuf);

    ProtocolSerializable response = null;
    switch (header.getOpcode()) {
    case OP_HELLO:
      response = new Hello(); break;
    case OP_ERROR:
      response = (ProtocolSerializable) new org.eproject.protocol.Error(); break;
    case OP_WELCOME:
      response = new Welcome(); break;
    case OP_GROUP_CREATED:
      response = new GroupCreated(); break;
    case OP_SUBSCRIBED:
      response = new Subscribed(); break;
      case OP_GROUP_STATE:
      response = new GroupState(); break;
    default:
      throw new RuntimeException(String.format("Unsupported opcode = %d", header.getOpcode().ordinal()));
    }

    response.deserialize(new InputBuffer(payloadBuf));

    return response;
  }

  public ProtocolSerializable sendHello(short version) throws Exception {
    Hello msg = new Hello();
    msg.setProtocolVersion(version);
    
    sendMessage(Opcode.OP_HELLO, msg);

    return readResponse();
  }

  public ProtocolSerializable sendCreateGroup(String name) throws Exception {
    CreateGroup msg = new CreateGroup();
    msg.setGroupName(name);

    sendMessage(Opcode.OP_CREATE_GROUP, msg);

    return readResponse();
  }

  public ProtocolSerializable sendSubscribe(String invitationKey, String name, byte[] picture) throws Exception {
    Subscribe msg = new Subscribe();
    msg.setInvitationKey(invitationKey);
    msg.setUserName(name);
    msg.setImageData(picture);

    sendMessage(Opcode.OP_SUBSCRIBE, msg);
    
    return readResponse();
  }

  public ProtocolSerializable sendShareLocation(double lat, double lon) throws Exception {
    ShareLocation msg = new ShareLocation();
    msg.setLocation(lon, lat);

    sendMessage(Opcode.OP_SHARE_LOCATION, msg);

    return readResponse();
  }
}
