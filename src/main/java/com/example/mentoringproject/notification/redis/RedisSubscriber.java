package com.example.mentoringproject.notification.redis;

import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisSubscriber implements MessageListener {

  private final ObjectMapper objectMapper;
  private final NotificationService notificationService;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {

      NotificationDto notificationDto = objectMapper.readValue(message.getBody(),
          NotificationDto.class);
      notificationService.send(notificationDto);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
