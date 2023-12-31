package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.entity.GroupMessage;
import com.example.mentoringproject.chat.entity.PrivateChatRoom;
import com.example.mentoringproject.chat.entity.PrivateMessage;
import com.example.mentoringproject.chat.model.GroupChatMessageInfo;
import com.example.mentoringproject.chat.model.PrivateChatMessageInfo;
import com.example.mentoringproject.chat.model.PrivateChatRoomCreateRequest;
import com.example.mentoringproject.chat.model.PrivateChatRoomCreateResponse;
import com.example.mentoringproject.chat.model.PrivateMyChatListInfo;
import com.example.mentoringproject.chat.service.ChatService;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import com.example.mentoringproject.user.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

  private final ChatService chatService;
  private final UserService userService;
  private final UserRepository userRepository;

  /*
  // 그룹 채팅방 생성 (사용 안함)
  @Operation(summary = "그룹 채팅방 생성 api", description = "그룹 채팅방 생성 api", responses = {
      @ApiResponse(responseCode = "200", description = "그룹 채팅방 등록 성공", content =
      @Content(schema = @Schema(implementation = ChatRoomCreateResponse.class)))
  })
  @PostMapping("/{mentoringId}")
  public ResponseEntity<ChatRoomCreateResponse> createRoom(@PathVariable Long mentoringId) {
    return ResponseEntity.ok(
        ChatRoomCreateResponse.fromEntity(chatService.createRoom(mentoringId)));
  }
   */

  // 그룹 채팅방 입장 후 조회
  @Operation(summary = "그룹 채팅방 입장 후 조회 api", description = "그룹 채팅방 입장 후 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "그룹 채팅방 입장 후 조회 성공", content =
      @Content(schema = @Schema(implementation = GroupChatMessageInfo.class)))
  })
  @GetMapping("/{mentoringId}")
  public ResponseEntity<List<GroupChatMessageInfo>> GroupMessageInfo(
      @PathVariable Long mentoringId) {

    List<GroupMessage> groupMessageList = chatService.findAllGroupMessages(mentoringId);

    List<GroupChatMessageInfo> messageInfos = groupMessageList.stream()
        .map(message -> GroupChatMessageInfo.fromEntity(message, userRepository))
        .collect(Collectors.toList());

    return ResponseEntity.ok(messageInfos);

  }


  // 1:1 채팅방 생성
  @Operation(summary = "1:1 채팅방 생성 api", description = "1:1 채팅방 생성 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 채팅방 등록 성공", content =
      @Content(schema = @Schema(implementation = PrivateChatRoomCreateResponse.class)))
  })
  @PostMapping("/private")
  public ResponseEntity<PrivateChatRoomCreateResponse> privateCreateChatRoom(@RequestBody
  PrivateChatRoomCreateRequest privateChatRoomCreateRequest) {
    String email = SpringSecurityUtil.getLoginEmail();
    User user = userService.getUser(email);

    PrivateChatRoom privateChatRoom = chatService.createPrivateRoom(privateChatRoomCreateRequest,
        user.getId());
    PrivateChatRoomCreateResponse privateRoomCreateResponse = new PrivateChatRoomCreateResponse(
        privateChatRoom.getUser().getId(),
        privateChatRoom.getMentor().getId(),
        privateChatRoom.getMentoring().getId(),
        privateChatRoom.getRegisterDatetime(),
        true,
        privateChatRoom.getId()
    );
    return ResponseEntity.ok(privateRoomCreateResponse);
  }

  // 1:1 채팅방 입장 후 조회
  @Operation(summary = "1:1 채팅방 입장 후 조회 api", description = "1:1 채팅방 입장 후 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 채팅방 입장 후 조회 성공", content =
      @Content(schema = @Schema(implementation = PrivateChatMessageInfo.class)))
  })
  @GetMapping("/private/{privateChatRoomId}")
  public ResponseEntity<List<PrivateChatMessageInfo>> privateMessageInfo(
      @PathVariable Long privateChatRoomId) {
    List<PrivateMessage> privateMessageList = chatService.findAllPrivateMessages(privateChatRoomId);

    List<PrivateChatMessageInfo> messageInfos = privateMessageList.stream()
        .map(message -> PrivateChatMessageInfo.fromEntity(message, userRepository))
        .collect(Collectors.toList());

    return ResponseEntity.ok(messageInfos);
  }


  // 사용자의 1:1 채팅방 리스트 가져오기
  @Operation(summary = "사용자의 1:1 채팅방 리스트 가져오기 api", description = "사용자의 1:1 채팅방 리스트 가져오기 api", responses = {
      @ApiResponse(responseCode = "200", description = "사용자의 1:1 채팅방 리스트 가져오기 성공", content =
      @Content(schema = @Schema(implementation = PrivateMyChatListInfo.class)))
  })
  @GetMapping("/private/chatList")
  public ResponseEntity<List<PrivateMyChatListInfo>> privateMyChatListInfo() {
    String email = SpringSecurityUtil.getLoginEmail();
    log.debug("Debug: email - {}", email);

    User user = userService.getUser(email);

    String nickName = user.getNickName();
    log.debug("Debug: nickName - {}", nickName);

    List<PrivateMessage> privateMessageList = chatService.getPrivateMyChatListInfo(email);

    log.debug("Debug: privateMessageList - {}", privateMessageList);

    List<PrivateMyChatListInfo> chatListInfo = getLatestChatListInfo(privateMessageList, nickName);

    log.debug("Debug: chatListInfo - {}", chatListInfo);

    chatListInfo.sort(Comparator.comparing(PrivateMyChatListInfo::getRegisterDatetime).reversed());

    log.debug("Debug: chatListInfo - {}", chatListInfo);

    return ResponseEntity.ok(chatListInfo);
  }

  private List<PrivateMyChatListInfo> getLatestChatListInfo(List<PrivateMessage> messages, String nickName) {
    Map<PrivateChatRoom, PrivateMessage> latestMessagesMap = new HashMap<>();

    for (PrivateMessage message : messages) {
      PrivateChatRoom chatRoom = message.getPrivateChatRoom();

      if (!latestMessagesMap.containsKey(chatRoom) ||
          message.getRegisterDatetime().isAfter(latestMessagesMap.get(chatRoom).getRegisterDatetime())) {
        latestMessagesMap.put(chatRoom, message);
      }
    }

    return latestMessagesMap.values().stream()
        .map(message -> PrivateMyChatListInfo.fromEntity(message, nickName))
        .collect(Collectors.toList());
  }

}

