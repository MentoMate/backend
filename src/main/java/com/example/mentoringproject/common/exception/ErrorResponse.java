package com.example.mentoringproject.common.exception;

public class ErrorResponse {
  private String errorMessage;
  private Long privateChatRoomId;

  public ErrorResponse(String errorMessage, Long privateChatRoomId) {
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
