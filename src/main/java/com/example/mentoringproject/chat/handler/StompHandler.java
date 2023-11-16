package com.example.mentoringproject.chat.handler;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

  private final JwtService jwtService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
    log.debug("Start stompHeaderAccessor");
    if (stompHeaderAccessor.getCommand() == StompCommand.CONNECT) {
      String authorizationHeader = stompHeaderAccessor.getFirstNativeHeader("Authorization");
      log.debug("Get AuthorizationHeader: {}", authorizationHeader);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String token = authorizationHeader.substring(7);
        log.debug("Get ACCESSESTOKEN Get: {}", token);
        if (!jwtService.isTokenValid(token)) {
          throw new AppException(HttpStatus.BAD_REQUEST, "INVALID TOKEN");
        }
      } else {
        throw new AppException(HttpStatus.UNAUTHORIZED, "MISSING OR MALFORMED TOKEN");
      }
      log.debug("SUCCESS TO VALID TOKEN");
    }
    log.debug("PreSend message");
    return message;
  }
}