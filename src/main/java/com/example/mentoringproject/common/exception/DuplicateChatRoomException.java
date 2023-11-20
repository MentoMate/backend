package com.example.mentoringproject.common.exception;

import lombok.Getter;

@Getter
public class DuplicateChatRoomException extends RuntimeException {
  private final long roomId;

  public DuplicateChatRoomException(long roomId, String message) {
    super(message);
    this.roomId = roomId;
  }

  public long getRoomId() {
    return roomId;
  }

  public RoomErrorResponse getErrorResponse() {
    return new RoomErrorResponse(getMessage(), roomId);
  }
}
