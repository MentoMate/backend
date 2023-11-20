package com.example.mentoringproject.common.exception;

import org.springframework.http.HttpStatus;

public class ChatRoomAlreadyExistsException extends RuntimeException {
  private final Long privateChatRoomId;

  public ChatRoomAlreadyExistsException(Long privateChatRoomId, String message) {
    super(message);
    this.privateChatRoomId = privateChatRoomId;
  }

  public Long getPrivateChatRoomId() {
    return privateChatRoomId;
  }

  public RoomErrorResponse makeRoomErrorResponse() {
    return new RoomErrorResponse(getMessage(), privateChatRoomId);
  }
}
