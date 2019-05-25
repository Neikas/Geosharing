package org.eproject.protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.eproject.protocol.helpers.InputBuffer;
import org.eproject.protocol.*;
import org.eproject.protocol.Error;
import org.eproject.protocol.core.Opcode;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ComplexSerDeTest {
  @Test
  void shouldSerDeErrorBody() {
    Error input = new Error();
    input.setErrorCode(123);
    byte[] bytes = input.serialize();

    Error output = new Error();

    output.deserialize(new InputBuffer(bytes));

    assertEquals(123, output.getErrorCode());
  }

  @Test
  void shouldSerDeGroupCreatedBody() {
    GroupCreated input = new GroupCreated();
    input.setInvitationKey("abc:123456");
    byte[] bytes = input.serialize();

    GroupCreated output = new GroupCreated();

    output.deserialize(new InputBuffer(bytes));

    assertEquals("abc:123456", output.getInvitationKey());
  }

  @Test
  void shouldSerDeHeader() {
    Header input = new Header();
    input.setOpcode(Opcode.OP_WELCOME);
    input.setPayloadSize(543);
    byte[] bytes = input.serialize();

    Header output = new Header();

    output.deserialize(new InputBuffer(bytes));

    assertEquals(Opcode.OP_WELCOME, output.getOpcode());
    assertEquals(543, output.getPayloadSize());
  }

  @Test
  void shouldSerDeObjectList() {
    ObjectList<Header> input = new ObjectList<>(Header.class);
    Header header1 = new Header();
    header1.setOpcode(Opcode.OP_WELCOME);
    header1.setPayloadSize(432);

    Header header2 = new Header();
    header2.setOpcode(Opcode.OP_ERROR);
    header2.setPayloadSize(987);

    input.asList().add(header1);
    input.asList().add(header2);
    byte[] bytes = input.serialize();

    ObjectList<Header> output = new ObjectList<>(Header.class);
    output.deserialize(new InputBuffer(bytes));
    List<Header> outputList = output.asList();

    assertEquals(2, outputList.size());
    assertEquals(Opcode.OP_WELCOME, outputList.get(0).getOpcode());
    assertEquals(432, outputList.get(0).getPayloadSize());
    assertEquals(Opcode.OP_ERROR, outputList.get(1).getOpcode());
    assertEquals(987, outputList.get(1).getPayloadSize());
  }

  @Test
  void shouldSerDeSubscribed() {
    Subscribed input = new Subscribed();
    input.setUserId(123);
    byte[] bytes = input.serialize();

    Subscribed output = new Subscribed();
    output.deserialize(new InputBuffer(bytes));

    assertEquals(123, output.getUserId());
  }

  @Test
  void shouldSerDeHello() {
    Hello input = new Hello();
    input.setProtocolVersion((short)543);
    byte[] bytes = input.serialize();

    Hello output = new Hello();
    output.deserialize(new InputBuffer(bytes));

    assertEquals(543, output.getProtocolVersion());
  }

  @Test
  void shouldSerDeCreateGroup() {
    CreateGroup input = new CreateGroup();
    input.setGroupName("test-group");
    byte[] bytes = input.serialize();

    CreateGroup output = new CreateGroup();
    output.deserialize(new InputBuffer(bytes));

    assertEquals("test-group", output.getGroupName());
  }
}
