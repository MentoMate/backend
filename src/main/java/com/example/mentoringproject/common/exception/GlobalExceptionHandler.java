package com.example.mentoringproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final String VALIDATION_ERROR_CODE = "VALIDATION ERROR CODE";

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> appExceptionHandler(AppException e) {
    return ResponseEntity.status(e.getErrorCode()).body(makeErrorResponse(e.getErrorCode(), e.getMessage()));
  }

  private ErrorResponse makeErrorResponse(HttpStatus status, Object errorMessage) {
    return new ErrorResponse(status.toString(), errorMessage);
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(
      MissingServletRequestPartException ex) {
    // 예외 처리 및 클라이언트에게 오류 응답 전달
    String errorMessage = "Required request part '" + ex.getRequestPartName() + "' is not present";
    return ResponseEntity.badRequest().body(makeErrorResponse(
        HttpStatus.BAD_REQUEST, errorMessage));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidException(
      MethodArgumentNotValidException exception) {
    BindingResult bindingResult = exception.getBindingResult();
    StringBuilder builder = new StringBuilder();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder.append("[");
      builder.append(fieldError.getField());
      builder.append("](은)는 ");
      builder.append(fieldError.getDefaultMessage());
      builder.append(" 입력된 값: [");
      builder.append(fieldError.getRejectedValue());
      builder.append("]");
    }

    ErrorResponse result = ErrorResponse.builder()
        .code(VALIDATION_ERROR_CODE)
        .message(builder.toString())
        .build();

    return ResponseEntity.badRequest().body(result);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException exception) {
    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    StringBuilder builder = new StringBuilder();
    for (ConstraintViolation constraintViolation : constraintViolations) {
      builder.append("[");
      builder.append(constraintViolation.getMessage());
      builder.append("]");
    }
    ErrorResponse result = ErrorResponse.builder()
        .code(VALIDATION_ERROR_CODE)
        .message(builder.toString())
        .build();

    return ResponseEntity.badRequest().body(result);
  }

  @ExceptionHandler(ChatRoomAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleChatRoomAlreadyExistsException(ChatRoomAlreadyExistsException ex) {
    RoomErrorResponse errorResponse = ex.makeRoomErrorResponse();
    return ResponseEntity.badRequest().body(makeErrorResponse(HttpStatus.BAD_REQUEST, errorResponse));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.badRequest().body(makeErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
    return ResponseEntity.badRequest().body(makeErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }
}
