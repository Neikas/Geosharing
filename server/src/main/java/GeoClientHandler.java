package org.eproject.server;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.handler.timeout.IdleStateEvent;
import models.User;
import models.Group;

import controllers.GroupController;
import controllers.UserController;
import org.eproject.protocol.*;
import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.exceptions.GroupNotJoined;
import org.eproject.protocol.exceptions.InternalError;
import org.eproject.protocol.exceptions.InvalidProtocolVersion;


class GeoClientHandler extends ChannelInboundHandlerAdapter {
  private static final int PROTOCOL_VERSION = 0x1;
  private static final Logger logger = Logger.getLogger(GeoClientHandler.class.getName());
  private static final GroupController groupController = new GroupController();
  private static final UserController userController = new UserController();
  private long lastRequest = 0; //Timestamp of last GetGroupMembers req.
  private User user = null;

  /**
   * verifies clients' protocol version
   * replies with the "welcome" message if protocol version is supported
   * 
   * @param message Hello message that contains client protocol version
   * @throws UnsupportedProtocolVersion if client version is not supported
   */
  private Welcome checkClientVersion(Hello message) {
    if (message.getProtocolVersion() != PROTOCOL_VERSION) {
      throw new InvalidProtocolVersion();
    }

    return new Welcome();
  }

  private GroupCreated createGroup(CreateGroup message) {
    String invitationKey = groupController.createGroup(message.getGroupName());
    GroupCreated reply = new GroupCreated();
    reply.setInvitationKey(invitationKey);

    return reply;
  }

  private Subscribed subscribe(Subscribe message) {
    String invitationKey = message.getInvitationKey();
    Group group = groupController.findByInvitationKey(invitationKey);
    // At this point we are sure that group exists
    user = userController.create(message.getUserName(), message.getImageData());
    // Exit the group if user already a member of some group
    Group joinedGroup = groupController.getGroup(user);
    if (joinedGroup != null) {
      groupController.exitGroup(joinedGroup, user);
    }
    groupController.addMember(group, user);
    lastRequest = 0;
    
    Subscribed reply = new Subscribed();
    reply.setUserId(user.getId());

    return reply;
  }

  private GroupState shareLocation(ShareLocation message) {
    Group joinedGroup = groupController.getGroup(user);
    if (joinedGroup == null) {
      throw new GroupNotJoined();
    }
    userController.updateLocation(user, message.getLatitude(), message.getLongitude());

    GroupState reply = new GroupState();
    // Prepare list of new members
    List<User> newMembers = groupController.getNewMembers(joinedGroup, user, lastRequest);
    newMembers.forEach((member) -> {
      UserProfile memberProfile = new UserProfile();
      memberProfile.setUserId(member.getId());
      memberProfile.setPicture(member.getPicture());
      memberProfile.setUserName(member.getName());

      reply.addNewMember(memberProfile);
    });

    // Members location
    groupController.getMembers(joinedGroup).forEach((member) -> {
      reply.addMemberLocation(new MemberLocation(member.getId(), member.getLat(), member.getLon()));
    });

    lastRequest = Instant.now().toEpochMilli();

    return reply;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ProtocolSerializable reply = null;

    if (msg instanceof Hello) {
      reply = checkClientVersion((Hello) msg);
    } else if (msg instanceof Subscribe) {
      reply = subscribe((Subscribe) msg);
    } else if (msg instanceof CreateGroup) {
      reply = createGroup((CreateGroup) msg);
    } else if (msg instanceof ShareLocation) {
      reply = shareLocation((ShareLocation) msg);
    } else {
      throw new InternalError(String.format("Unsupported message type - '%s'", msg));
    }

    ctx.writeAndFlush(reply);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
    if (evt instanceof IdleStateEvent) {
      Group joinedGroup = groupController.getGroup(user);
      // exit the group
      if (joinedGroup != null) {
        groupController.exitGroup(joinedGroup, user);
      }

      // close socket connection
      ctx.close();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.warning("Exception: " + cause.toString());

    org.eproject.protocol.Error message = new org.eproject.protocol.Error();
    message.setErrorCode(cause);

    ctx.writeAndFlush(message);
    ctx.close();
  }
}
