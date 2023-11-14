package com.example.mentoringproject.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mentoringproject.chat.entity.GroupChatRoom;

public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, String> {

}

