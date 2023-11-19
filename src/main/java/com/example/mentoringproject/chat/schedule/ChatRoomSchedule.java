package com.example.mentoringproject.chat.schedule;

import com.example.mentoringproject.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomSchedule {

  private final ChatService chatService;

  @Scheduled(cron = "0 0 0 * * ?")
  public void createChatRoomScheduled() {
    chatService.createRoomAutomatically();
  }
}
