package org.eproject.protocol;

import org.eproject.protocol.helpers.OutputBuffer;

import org.eproject.protocol.core.ProtocolSerializable;
import org.eproject.protocol.core.ReadableBuffer;
import org.eproject.protocol.core.WritableBuffer;
import org.eproject.protocol.exceptions.*;

/**
 * Body of the error message
 * Reply when request failed to process.
 *
 * Structure:
 *  - error code: int
 */
final public class Error implements ProtocolSerializable {
  private int errorCode;

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * set error code based on exception. All unknown exceptions are treated as "InternalError"
   *
   * @param cause Exception to check
   */
  public void setErrorCode(Throwable cause) {
    if (cause instanceof org.eproject.protocol.exceptions.InternalError) {
      setErrorCode(ErrorCode.INTERNAL_ERROR.ordinal());
    } else if (cause instanceof InvalidBufferSize) {
      setErrorCode(ErrorCode.INVALID_BUFFER_SIZE.ordinal());
    } else if (cause instanceof InvalidData) {
      setErrorCode(ErrorCode.INVALID_DATA.ordinal());
    } else if (cause instanceof InvalidProtocolVersion) {
      setErrorCode(ErrorCode.INVALID_PROTOCOL_VERSION.ordinal());
    } else if (cause instanceof GroupNotFound) {
      setErrorCode(ErrorCode.GROUP_NOT_FOUND.ordinal());
    } else if (cause instanceof GroupNotJoined) {
      setErrorCode(ErrorCode.GROUP_NOT_JOINED.ordinal());
    } else {
      setErrorCode(ErrorCode.INTERNAL_ERROR.ordinal());
    }
  }

  @Override
  public void deserialize(ReadableBuffer buffer) {
    setErrorCode(buffer.getUByte());
  }

  @Override
  public byte[] serialize() {
    WritableBuffer buf = new OutputBuffer();
    buf.writeUByte(getErrorCode());

    return buf.getBytes();
  }
}

enum ErrorCode {
  INTERNAL_ERROR,
  INVALID_BUFFER_SIZE,
  INVALID_DATA,
  INVALID_PROTOCOL_VERSION,
  GROUP_NOT_FOUND,
  GROUP_NOT_JOINED
}

