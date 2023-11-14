package com.example.mentoringproject.chat.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PrivateMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "private_chat_message_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "private_chat_room_id")
  private PrivateChatRoom privateChatRoom;

  @JoinColumn(name = "sender_nickname")
  private String senderNickName;

  @Lob
  @Column(name = "message")
  private String message;

  @CreatedDate
  private LocalDateTime registerDatetime;

  protected PrivateMessage() {

  }

  public PrivateMessage(PrivateChatRoom privateChatRoom, String senderNickName, String message) {
    this.privateChatRoom = privateChatRoom;
    this.senderNickName = senderNickName;
    this.message = message;
  }

}