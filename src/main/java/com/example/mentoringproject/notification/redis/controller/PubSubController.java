package com.example.mentoringproject.notification.redis.controller;

import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.redis.RedisPublisher;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publish/notification")
@AllArgsConstructor
public class PubSubController {

    private final ChannelTopic myTopic;
    private final RedisPublisher redisPublisher;

    @PostMapping
    public void pushMessage(@RequestBody NotificationDto notificationDto) {
        //1. 받는 사람의 아이디 값과 메시지를 담아서 전송
        redisPublisher.publishNotification(myTopic, notificationDto);
    }

}
