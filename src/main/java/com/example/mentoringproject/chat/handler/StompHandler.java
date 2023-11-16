package com.example.mentoringproject.chat.handler;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

  private final JwtService jwtService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
    if (stompHeaderAccessor.getCommand() == StompCommand.CONNECT) {
      String accessToken = stompHeaderAccessor.getFirstNativeHeader("AccessToken");
      if (!jwtService.isTokenValid(accessToken)) {
        throw new AppException(HttpStatus.BAD_REQUEST, "INVALID TOKEN");
      }
    }
    return message;
  }
}