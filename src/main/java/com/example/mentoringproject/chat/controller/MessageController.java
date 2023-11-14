package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.entity.GroupMessage;
import com.example.mentoringproject.chat.model.GroupChatMessage;
import com.example.mentoringproject.chat.model.PrivateChatMessage;
import com.example.mentoringproject.chat.model.PrivateChatMessageInfo;
import com.example.mentoringproject.chat.repository.GroupMessageRepository;
import com.example.mentoringproject.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final SimpMessageSendingOperations sendingOperations;
  private final ChatService chatService;

  // 그룹 메세지 저장
  @Operation(summary = "그룹 메세지 저장 api", description = "그룹 메세지 저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "그룹 메세지 저장 성공")
  })
  @MessageMapping("/chat/message/group")
  public void enter(GroupChatMessage groupChatMessage) {
    chatService.saveGroupChatMessage(groupChatMessage);
    sendingOperations.convertAndSend("/topic/chat/room/" + groupChatMessage.getGroupMentoringId(), groupChatMessage);

  }

  // 1:1 메세지 저장
  @Operation(summary = "1:1 메세지 저장 api", description = "1:1 메세지 저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 메세지 저장 성공")
  })
  @MessageMapping("/chat/message/private")
  public void enter(PrivateChatMessage privateChatMessage) {
    chatService.savePrivateChatMessage(privateChatMessage);
    sendingOperations.convertAndSend("/subscribe/chat/room/" + privateChatMessage.getPrivateChatRoomId(), privateChatMessage);
  }
}
