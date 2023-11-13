package com.example.mentoringproject.chat.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Enumerated(value = EnumType.STRING)
  private MessageType type;
  //채팅방 ID
  private Long scheduleId;
  //보내는 사람
  private String sender;
  //내용
  private String message;

  @CreatedDate
  private LocalDateTime registerDatetime;

}