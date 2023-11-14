package com.example.mentoringproject.notification.redis;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.notification.notification.model.NotificationDto;
import com.example.mentoringproject.notification.notification.model.NotificationResponseDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RedisSubscriber implements MessageListener {

  private final ObjectMapper objectMapper;
  private final NotificationService notificationService;

  @Override
  public void onMessage(Message message, byte[] pattern) {

    try {
      log.debug("redis Listener ok");
      NotificationResponseDto notificationResponseDto = objectMapper.readValue(message.getBody(),
          NotificationResponseDto.class);
      log.debug("send message to sseUser, message= {}", notificationResponseDto.toString());
      notificationService.send(notificationResponseDto);
    } catch (IOException e) {
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
