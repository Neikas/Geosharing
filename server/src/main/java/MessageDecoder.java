package org.eproject.server;

import org.eproject.protocol.*;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.helpers.*;
import org.eproject.protocol.exceptions.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;
import java.util.logging.Logger;

public class MessageDecoder extends ReplayingDecoder<Void> {
  private static final Logger logger = Logger.getLogger(MessageDecoder.class.getName());

  private byte[] readBytes(final ByteBuf buf, int count) throws Exception {
    ByteBuf bb = buf.readBytes(count);
    byte[] array = new byte[count];
    bb.getBytes(0, array);

    return array;
  }

  @Override
  protected void decode(final ChannelHandlerContext ctx, final ByteBuf buf,
                        final List<Object> out) throws Exception {
    // TODO: make it stateful without issuing Header decoder on each iteration

    byte[] headerBuf = readBytes(buf, Header.HEADER_LENGTH);
    Header header = new Header();
    header.deserialize(new InputBuffer(headerBuf));

    byte[] payloadBuf = readBytes(buf, header.getPayloadSize());

    logger.info(String.format("Header decoded: op = %s, ln = %d",
            header.getOpcode().toString(), header.getPayloadSize()));

    ProtocolSerializable payload = null;
    switch (header.getOpcode()) {
      case OP_HELLO:
        payload = new Hello();
        break;
      case OP_SUBSCRIBE:
        payload = new Subscribe();
        break;
      case OP_CREATE_GROUP:
        payload = new CreateGroup();
        break;
      case OP_SHARE_LOCATION:
        payload = new ShareLocation();
        break;
      default:
        throw new InvalidData("Unsupported opcode");
    }

    payload.deserialize(new InputBuffer(payloadBuf));
    out.add(payload);
  }
}
