package com.example.mentoringproject.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringproject.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

}
