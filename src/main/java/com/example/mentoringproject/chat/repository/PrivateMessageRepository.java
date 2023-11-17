package com.example.mentoringproject.chat.repository;

import com.example.mentoringproject.chat.entity.PrivateMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

  List<PrivateMessage> findByPrivateChatRoomIdOrderByRegisterDatetime(Long chatRoomId);
  List<PrivateMessage> findByPrivateChatRoomId(Long privateChatRoomId);



}