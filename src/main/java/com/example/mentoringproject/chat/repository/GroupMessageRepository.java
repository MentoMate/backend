package com.example.mentoringproject.chat.repository;

import com.example.mentoringproject.chat.entity.GroupMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
  List<GroupMessage> findByMentoringIdOrderByRegisterDatetime(Long mentoringId);
}
