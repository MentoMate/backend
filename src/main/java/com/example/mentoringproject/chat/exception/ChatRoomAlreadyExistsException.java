package com.example.mentoringproject.chat.exception;

public class ChatRoomAlreadyExistsException extends RuntimeException {
  private Long privateChatRoomId;

  public ChatRoomAlreadyExistsException(Long privateChatRoomId, String message) {
    super(message);
    this.privateChatRoomId = privateChatRoomId;
  }

  public Long getPrivateChatRoomId() {
    return privateChatRoomId;
  }

  public RoomErrorResponse getRoomErrorResponse() {
    return new RoomErrorResponse(getMessage(), privateChatRoomId);
  }
}