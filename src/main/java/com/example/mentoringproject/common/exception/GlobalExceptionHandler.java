package com.example.mentoringproject.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ChatRoomAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleChatRoomAlreadyExistsException(ChatRoomAlreadyExistsException ex) {
    ErrorResponse errorResponse = ex.getErrorResponse();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}

