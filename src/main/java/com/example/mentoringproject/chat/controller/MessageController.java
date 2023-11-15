package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.entity.GroupMessage;
import com.example.mentoringproject.chat.entity.PrivateMessage;
import com.example.mentoringproject.chat.model.GroupChatMessage;
import com.example.mentoringproject.chat.model.GroupChatMessageResponse;
import com.example.mentoringproject.chat.model.PrivateChatMessage;
import com.example.mentoringproject.chat.model.PrivateChatMessageResponse;
import com.example.mentoringproject.chat.model.PrivateChatRoomCreateResponse;
import com.example.mentoringproject.chat.service.ChatService;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
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
  private final UserService userService;

  // 그룹 메세지 저장
  @Operation(summary = "그룹 메세지 저장 api", description = "그룹 메세지 저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "그룹 메세지 저장 성공")
  })
  @MessageMapping("/chat/message/group")
  public void enter(GroupChatMessage groupChatMessage) {
    String email = SpringSecurityUtil.getLoginEmail();
    User user = userService.getUser(email);
    GroupMessage groupMessage = chatService.saveGroupChatMessage(groupChatMessage, user.getNickName());
    GroupChatMessageResponse groupChatMessageResponse = GroupChatMessageResponse.fromEntity(groupMessage);
    sendingOperations.convertAndSend("/topic/chat/room/" + groupChatMessage.getGroupMentoringId(),
        groupChatMessageResponse);

  }

  // 1:1 메세지 저장
  @Operation(summary = "1:1 메세지 저장 api", description = "1:1 메세지 저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 메세지 저장 성공")
  })
  @MessageMapping("/chat/message/private")
  public void enter(PrivateChatMessage privateChatMessage) {
    String email = SpringSecurityUtil.getLoginEmail();
    User user = userService.getUser(email);
    PrivateMessage privateMessage = chatService.savePrivateChatMessage(privateChatMessage,
        user.getNickName());
    PrivateChatMessageResponse privateChatMessageResponse = PrivateChatMessageResponse.fromEntity(privateMessage);
    sendingOperations.convertAndSend(
        "/subscribe/chat/room/" + privateChatMessage.getPrivateChatRoomId(), privateChatMessageResponse);
  }
}
