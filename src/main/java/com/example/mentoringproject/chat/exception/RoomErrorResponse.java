package com.example.mentoringproject.chat.exception;

public class RoomErrorResponse {
  private String errorMessage;
  private Long privateChatRoomId;

  public RoomErrorResponse(String errorMessage, Long privateChatRoomId) {
    this.errorMessage = errorMessage;
    this.privateChatRoomId = privateChatRoomId;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Long getPrivateChatRoomId() {
    return privateChatRoomId;
  }
}