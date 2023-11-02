package com.example.mentoringproject.notification.redis;

import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;

    public void publishNotification(ChannelTopic topic, NotificationDto notificationDto) {
        notificationService.saveNotification(notificationDto);
        redisTemplate.convertAndSend(topic.getTopic(), notificationDto);
    }

}
