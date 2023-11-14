package com.example.mentoringproject.chat.schedule;

import com.example.mentoringproject.chat.service.ChatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomSchedule {

  private final ChatService chatService;

  public ChatRoomSchedule(ChatService chatService) {
    this.chatService = chatService;
  }

  // 매일 자정에 스케줄링 실행 (cron 표현식 사용)
  @Scheduled(cron = "0 0 0 * * ?")
  public void createChatRoomScheduled() {
    chatService.createRoomAutomatically();
  }
}
