package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MentoringInfo {
  private long mentoringId;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;
  private String uploadUrl;
  private String uploadFolder;
  private int countWatch;
  private Long userId;
  private String name;
  private String thumbNailImg;
  private int leftPeople;
  private int followers;
  private boolean isOwner;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static MentoringInfo from(Mentoring mentoring, boolean isOwner) {

    return MentoringInfo.builder()
        .mentoringId(mentoring.getId())
        .title(mentoring.getTitle())
        .content(mentoring.getContent())
        .startDate(mentoring.getStartDate())
        .endDate(mentoring.getEndDate())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .uploadFolder(mentoring.getUploadFolder())
        .uploadUrl(mentoring.getUploadUrl())
        .userId(mentoring.getUser().getId())
        .name(mentoring.getUser().getName())
        .countWatch(mentoring.getCountWatch())
        .thumbNailImg(mentoring.getUploadUrl())
        .leftPeople(mentoring.getNumberOfPeople() - mentoring.getMenteeList().size())
        .followers(mentoring.getFollowerList().size())
        .isOwner(isOwner)
        .registerDate(mentoring.getRegisterDate())
        .updateDate(mentoring.getUpdateDate())
        .build();
  }

}
