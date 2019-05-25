package org.eproject.server;

import org.eproject.protocol.*;
import org.eproject.protocol.core.Opcode;
import org.eproject.protocol.core.ProtocolSerializable;
import java.util.logging.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Object> {
  /**
   * Identifies opcoder by instance type
   */
  private static final Logger logger = Logger.getLogger(MessageEncoder.class.getName());
  private Opcode getOpcode(Object msg) {
    if (msg instanceof Welcome) {
      return Opcode.OP_WELCOME;
    } else if (msg instanceof org.eproject.protocol.Error) {
      return Opcode.OP_ERROR;
    } else if (msg instanceof GroupCreated) {
      return Opcode.OP_GROUP_CREATED;
    } else if (msg instanceof Subscribed) {
      return Opcode.OP_SUBSCRIBED;
    } else if (msg instanceof GroupState) {
      return Opcode.OP_GROUP_STATE;
    } else {
      throw new RuntimeException("Encoder. Unknown message");
    }
  }

  @Override
  public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
    Opcode opcode = getOpcode(msg);
    logger.info(String.format("Encoding opcode = %s", opcode.toString()));

    ProtocolSerializable payload = (ProtocolSerializable) msg;
    byte[] payloadBuf = payload.serialize();

    Header header = new Header();
    header.setOpcode(opcode);
    header.setPayloadSize(payloadBuf.length);
    byte[] headerBuf = header.serialize();

    out.writeBytes(headerBuf);
    out.writeBytes(payloadBuf);
  }
}
