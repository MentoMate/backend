package com.example.mentoringproject.mentoring.entity;

import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.user.entity.SocialType;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "mentoring")
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
  private String imgUrl;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private int countWatch;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static Mentoring from(User user, MentoringDto mentoringDto) {
    return Mentoring.builder()
        .title(mentoringDto.getTitle())
        .content(mentoringDto.getContent())
        .startDate(mentoringDto.getStartDate())
        .endDate(mentoringDto.getEndDate())
        .numberOfPeople(mentoringDto.getNumberOfPeople())
        .amount(mentoringDto.getAmount())
        .status(mentoringDto.getStatus())
        .category(mentoringDto.getCategory())
        .imgUrl(mentoringDto.getImgUrl())
        .registerDate(LocalDateTime.now())
        .user(user)
        .build();
  }

}
