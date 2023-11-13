package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.entity.ChatMessage;
import com.example.mentoringproject.chat.entity.MessageType;
import com.example.mentoringproject.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final SimpMessageSendingOperations sendingOperations;
  private final ChatMessageRepository chatMessageRepository;

  @MessageMapping("/chat/message")
  public void enter(ChatMessage message) {
   // if (MessageType.ENTER.equals(message.getType())) {
   //   message.setMessage(message.getSender() + "님이 입장하였습니다.");
   // }
    sendingOperations.convertAndSend("/topic/chat/room/" + message.getScheduleId(), message);

    chatMessageRepository.save(message);
  }
}