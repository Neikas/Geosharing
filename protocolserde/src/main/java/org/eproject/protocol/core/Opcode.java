package org.eproject.protocol.core;

/**
 * List of supported operation codes
 */
public enum Opcode {
  OP_ERROR,
  OP_HELLO,
  OP_WELCOME,
  OP_CREATE_GROUP,
  OP_GROUP_CREATED,
  OP_SUBSCRIBE,
  OP_SUBSCRIBED,
  OP_SHARE_LOCATION,
  OP_GROUP_STATE
}
