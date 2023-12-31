package com.example.mentoringproject.mentoring.mentoring.entity;

import com.example.mentoringproject.chat.entity.GroupChatRoom;
import com.example.mentoringproject.chat.entity.GroupMessage;
import com.example.mentoringproject.mentoring.mentoring.model.MentoringSave;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "mentoring")
@EntityListeners(AuditingEntityListener.class)
public class Mentoring {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mentoring_id")
  private Long id;

  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;

  @Enumerated(EnumType.STRING)
  private MentoringStatus status;
  private String category;

  private String uploadUrl;
  private String uploadFolder;
  private Boolean roomExist;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private int countWatch;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "mentoring_follower",
      joinColumns = @JoinColumn(name = "mentoring_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> followerList = new ArrayList<>();

  @OneToOne(mappedBy = "mentoring")
  private GroupChatRoom groupChatRoom;

  @OneToMany(mappedBy = "mentoring", cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
  List<GroupMessage> groupMessageList = new ArrayList<>();

  @CreatedDate
  private LocalDateTime registerDate;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;


  public static Mentoring from(User user, MentoringSave mentoringSave) {
    return Mentoring.builder()
        .title(mentoringSave.getTitle())
        .content(mentoringSave.getContent())
        .startDate(mentoringSave.getStartDate())
        .endDate(mentoringSave.getEndDate())
        .numberOfPeople(mentoringSave.getNumberOfPeople())
        .amount(mentoringSave.getAmount())
        .category(mentoringSave.getCategory())
        .uploadFolder(mentoringSave.getUploadFolder())
        .user(user)
        .build();
  }
}
