package com.example.mentoringproject.chat.repository;

import com.example.mentoringproject.chat.entity.PrivateChatRoom;
import com.example.mentoringproject.user.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChatRoomRepository extends JpaRepository<PrivateChatRoom, String> {
  Optional<PrivateChatRoom> findById(Long chatRoomId);
  boolean existsByUserIdAndMentoringId(Long userId, Long mentoringId);
  boolean existsByUser(User user);
  boolean existsByMentor(User user);
  PrivateChatRoom findFirstByUser(User user);
  PrivateChatRoom findFirstByMentor(User user);

}

