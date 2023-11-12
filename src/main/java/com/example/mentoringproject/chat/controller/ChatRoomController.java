package com.example.mentoringproject.chat.controller;

import com.example.mentoringproject.chat.service.ChatService;
import com.example.mentoringproject.chat.entity.ChatRoom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
  private final ChatService chatService;

  @GetMapping("/room")
  public String rooms(Model model) {
    return "/chat/room";
  }

  @GetMapping("/rooms")
  @ResponseBody
  public List<ChatRoom> room() {
    return chatService.findAllRoom();
  }

  @PostMapping("/room")
  @ResponseBody
  public ChatRoom createRoom(@RequestParam String name) {
    return chatService.createRoom(name);
  }

  // 자동으로 방 생성하는 메서드
  @GetMapping("/autoCreateRoom")
  public String autoCreateRoom() {
    LocalDate currentDate = LocalDate.now();

    // 예시: 특정 날짜에 자동으로 방 생성
    LocalDate targetDate = LocalDate.of(2023, 11, 15);

    if (currentDate.equals(targetDate)) {
      // 특정 날짜에 자동으로 방 생성
      chatService.createRoom("자동 생성된 방");
    }

    return "redirect:/chat/room";
  }

  @GetMapping("/room/enter/{roomId}")
  public String roomDetail(Model model, @PathVariable String roomId) {
    model.addAttribute("roomId", roomId);
    return "/chat/roomdetail";
  }

  @GetMapping("/room/{roomId}")
  @ResponseBody
  public ChatRoom roomInfo(@PathVariable String roomId) {
    return chatService.findById(roomId);
  }
}
