package com.example.mentoringproject.chat.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
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
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class GroupMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_chat_message_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoring;

  @JoinColumn(name = "sender_nickname")
  private String senderNickName;

  @Lob
  @Column(name = "message")
  private String message;

  @CreatedDate
  private LocalDateTime registerDatetime;

  protected GroupMessage(){

  }

  public GroupMessage(Mentoring mentoring, String senderNickName, String message) {
    this.mentoring = mentoring;
    this.senderNickName = senderNickName;
    this.message = message;
  }

}
