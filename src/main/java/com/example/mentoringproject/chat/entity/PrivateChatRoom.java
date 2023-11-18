package com.example.mentoringproject.chat.entity;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PrivateChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "private_chat_room_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "mentor_id")
  private User mentor;

  @ManyToOne
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoring;

  @CreatedDate
  private LocalDateTime registerDatetime;

  @OneToMany(mappedBy = "privateChatRoom", cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
  List<PrivateMessage> messageList = new ArrayList<>();

  protected PrivateChatRoom() {
  }

  public PrivateChatRoom(User user, User mentor, Mentoring mentoring) {
    this.user = user;
    this.mentor = mentor;
    this.mentoring = mentoring;
  }
}

