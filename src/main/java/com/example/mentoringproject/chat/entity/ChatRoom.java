package com.example.mentoringproject.chat.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long scheduleId;

  @CreatedDate
  private LocalDateTime registerDatetime;

  public static ChatRoom create(Long scheduleId) {
    ChatRoom room = new ChatRoom();
    room.scheduleId = scheduleId;
    return room;
  }
}
