package com.example.mentoringproject.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChatRoomGlobalExceptionHandler {

  @ExceptionHandler(ChatRoomAlreadyExistsException.class)
  public ResponseEntity<RoomErrorResponse> handleChatRoomAlreadyExistsException(ChatRoomAlreadyExistsException ex) {
    RoomErrorResponse errorResponse = ex.getRoomErrorResponse();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}