package com.example.mentoringproject.common.exception;

public class DuplicateChatRoomException extends RuntimeException {
  private final long roomId;

  public DuplicateChatRoomException(long roomId, String message) {
    super(message);
    this.roomId = roomId;
  }

  public long getRoomId() {
    return roomId;
  }
}
