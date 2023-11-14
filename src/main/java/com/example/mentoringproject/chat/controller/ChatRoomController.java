package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.entity.PrivateChatRoom;
import com.example.mentoringproject.chat.model.GroupChatMessageInfo;
import com.example.mentoringproject.chat.model.PrivateChatMessageInfo;
import com.example.mentoringproject.chat.model.PrivateChatRoomCreateRequest;
import com.example.mentoringproject.chat.model.PrivateChatRoomCreateResponse;
import com.example.mentoringproject.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

  private final ChatService chatService;

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
      @Content(schema = @Schema(implementation =GroupChatMessageInfo.class)))
  })
  @GetMapping("/{mentoringId}")
  public ResponseEntity<List<GroupChatMessageInfo>> GroupMessageInfo(
      @PathVariable Long mentoringId) {

    return ResponseEntity.ok(chatService.findAllGroupMessages(mentoringId).stream()
        .map(GroupChatMessageInfo::fromEntity).collect(Collectors.toList()));
  }


  // 1:1 채팅방 생성
  @Operation(summary = "1:1 채팅방 생성 api", description = "1:1 채팅방 생성 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 채팅방 등록 성공", content =
      @Content(schema = @Schema(implementation = PrivateChatRoomCreateResponse.class)))
  })
  @PostMapping("/private")
  public ResponseEntity<PrivateChatRoomCreateResponse> privateCreateChatRoom(@RequestBody
  PrivateChatRoomCreateRequest privateChatRoomCreateRequest) {
    PrivateChatRoom privateChatRoom = chatService.createPrivateRoom(privateChatRoomCreateRequest);
    PrivateChatRoomCreateResponse privateRoomCreateResponse = new PrivateChatRoomCreateResponse(
        privateChatRoom.getUser().getId(),
        privateChatRoom.getMentor().getId(),
        privateChatRoom.getMentoring().getId(),
        privateChatRoom.getRegisterDatetime()
    );
    return ResponseEntity.ok(privateRoomCreateResponse);
  }

  // 1:1 채팅방 입장 후 조회
  @Operation(summary = "1:1 채팅방 입장 후 조회 api", description = "1:1 채팅방 입장 후 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "1:1 채팅방 입장 후 조회 성공", content =
      @Content(schema = @Schema(implementation =PrivateChatMessageInfo.class)))
  })
  @GetMapping("/private/{privateChatRoomId}")
  public ResponseEntity<List<PrivateChatMessageInfo>> privateMessageInfo(
      @PathVariable Long privateChatRoomId) {
    return ResponseEntity.ok(chatService.findAllPrivateMessages(privateChatRoomId).stream()
        .map(PrivateChatMessageInfo::fromEntity).collect(Collectors.toList()));
  }
}

