package com.example.mentoringproject.chat.schedule;

import com.example.mentoringproject.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomSchedule {

  private final ChatService chatService;

  @Autowired
  public ChatRoomSchedule(ChatService chatService) {
    this.chatService = chatService;
  }

  @Scheduled(cron = "0 0 0 * * ?")
  public void createChatRoomScheduled() {
    chatService.createRoomAutomatically();
  }
}
