package com.example.mentoringproject.notification.redis;

import com.example.mentoringproject.notification.notification.model.NotificationRequestDto;
import com.example.mentoringproject.notification.notification.model.NotificationResponseDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;

    public void publishNotification(ChannelTopic topic, NotificationRequestDto parameter) {
        log.debug("notification save");
        NotificationResponseDto notificationResponseDto = notificationService.saveNotification(parameter);
        log.debug("notification convertAndSend to redis");
        redisTemplate.convertAndSend(topic.getTopic(), notificationResponseDto);
    }
}
