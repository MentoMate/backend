package com.example.mentoringproject.chat.service;

import com.example.mentoringproject.chat.entity.ChatMessage;
import com.example.mentoringproject.chat.entity.ChatRoom;
import com.example.mentoringproject.chat.repository.ChatMessageRepository;
import com.example.mentoringproject.chat.repository.ChatRoomRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

  private Map<String, ChatRoom> chatRooms;
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;


  @PostConstruct
  //의존관게 주입완료되면 실행되는 코드
  private void init() {
    chatRooms = new LinkedHashMap<>();
  }

  //채팅방 불러오기
  public List<ChatRoom> findAllRoom() {
    //채팅방 최근 생성 순으로 반환
    List<ChatRoom> result = new ArrayList<>(chatRooms.values());
    Collections.reverse(result);

    return result;
  }

  //채팅방 하나 불러오기 (필요할듯)
  public ChatRoom findById(Long scheduleId) {
    return chatRooms.get(scheduleId);
  }

  //채팅방 생성
  public ChatRoom createRoom(Long scheduleId) {
    try {
      ChatRoom chatRoom = ChatRoom.create(scheduleId);
      chatRoomRepository.save(chatRoom);
      return chatRoom;
    } catch (Exception e) {
      log.error("방 생성 중 오류: {}", e.getMessage(), e);
      throw e;
    }
  }

  public List<ChatMessage> findAllMessagesByScheduleId(Long scheduleId) {
    return chatMessageRepository.findAllByScheduleId(scheduleId);
  }
}