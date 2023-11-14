package com.example.mentoringproject.chat.repository;

import com.example.mentoringproject.chat.entity.PrivateChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatRoomRepository extends JpaRepository<PrivateChatRoom, String> {
  Optional<PrivateChatRoom> findById(Long chatRoomId);

}

