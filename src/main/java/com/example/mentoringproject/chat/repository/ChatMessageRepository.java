package com.example.mentoringproject.chat.repository;

import com.example.mentoringproject.chat.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findAllByScheduleId(Long ScheduleId);

}