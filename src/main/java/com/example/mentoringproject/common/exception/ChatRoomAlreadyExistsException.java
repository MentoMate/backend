package com.example.mentoringproject.common.exception;

public class ChatRoomAlreadyExistsException extends RuntimeException {
  private Long privateChatRoomId;

  public ChatRoomAlreadyExistsException(Long privateChatRoomId, String message) {
    super(message);
    this.privateChatRoomId = privateChatRoomId;
  }

  public Long getPrivateChatRoomId() {
    return privateChatRoomId;
  }

  public ErrorResponse getErrorResponse() {
    return new ErrorResponse(getMessage(), privateChatRoomId);
  }
}
