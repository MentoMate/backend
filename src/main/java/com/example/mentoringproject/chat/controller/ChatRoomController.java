package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.entity.ChatMessage;
import com.example.mentoringproject.chat.service.ChatService;
import com.example.mentoringproject.chat.entity.ChatRoom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
  private final ChatService chatService;

  // 채팅 리스트 화면(필요없을듯)
  @GetMapping("/room")
  public String rooms(Model model) {
    return "/chat/room";
  }
  // 모든 채팅방 목록 반환 (필요없을듯? 채팅방에 메시지 반환?)
  @GetMapping("/rooms")
  @ResponseBody
  public List<ChatRoom> room() {
    return chatService.findAllRoom();
  }
  // 채팅방 생성 (필요함)
  @PostMapping("/room/{scheduleId}")
  @ResponseBody
  public ChatRoom createRoom(@PathVariable Long scheduleId) {
    return chatService.createRoom(scheduleId);
  }
  // 채팅방 입장 화면
  @GetMapping("/room/enter/{scheduleId}")
  public String roomDetail(Model model, @PathVariable Long scheduleId) {
    model.addAttribute("scheduleId", scheduleId);
    return "/chat/roomdetail";
  }
  // 채팅방 입장 후 조회 (필요함)
  @GetMapping("/room/{scheduleId}")
  @ResponseBody
  public List<ChatMessage> roomInfo(@PathVariable Long scheduleId) {
    return chatService.findAllMessagesByScheduleId(scheduleId);
  }

}