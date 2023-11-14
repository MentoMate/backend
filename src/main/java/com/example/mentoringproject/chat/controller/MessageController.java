package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.model.GroupChatMessage;
import com.example.mentoringproject.chat.model.PrivateChatMessage;
import com.example.mentoringproject.chat.service.ChatService;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
  private final UserService userService;

  // 그룹 메세지 저장
  @Operation(summary = "그룹 메세지 저장 api", description = "그룹 메세지 저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "그룹 메세지 저장 성공")
  })
  @MessageMapping("/chat/message/group")
  public void enter(GroupChatMessage groupChatMessage) {
    String email = SpringSecurityUtil.getLoginEmail();
    User user = userService.getUser(email);
    groupChatMessage.setSenderNickName(user.getNickName());
    chatService.saveGroupChatMessage(groupChatMessage);
    sendingOperations.convertAndSend("/topic/chat/room/" + groupChatMessage.getGroupMentoringId(), groupChatMessage);

  }

  // 1:1 메세지 저장
  @Operation(summary = "1:1 메세지 저장 api", description = "1:1 메세지 저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 메세지 저장 성공")
  })
  @MessageMapping("/chat/message/private")
  public void enter(PrivateChatMessage privateChatMessage) {
    String email = SpringSecurityUtil.getLoginEmail();
    User user = userService.getUser(email);
    privateChatMessage.setSenderNickName(user.getNickName());
    chatService.savePrivateChatMessage(privateChatMessage);
    sendingOperations.convertAndSend("/subscribe/chat/room/" + privateChatMessage.getPrivateChatRoomId(), privateChatMessage);
  }
}
