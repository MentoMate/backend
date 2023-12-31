package com.example.mentoringproject.mentoring.mentoring.model;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
  private String uploadFolder;
  private String uploadUrl;
  private int countWatch;
  private Long userId;
  private String name;
  private String email;
  private String useProfileImg;
  private int leftPeople;
  private int followers;
  private boolean isOwner;
  private boolean isPrivateChatRoomCreate;
  private boolean isMentoringLike;
  private boolean isMentorFollow;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static MentoringInfo from(Mentoring mentoring, boolean isOwner,
      boolean isPrivateChatRoomCreate, boolean isMentoringLike, boolean isMentorFollow, List<User> userList ) {

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
        .email(mentoring.getUser().getEmail())
        .name(mentoring.getUser().getName())
        .countWatch(mentoring.getCountWatch())
        .useProfileImg(mentoring.getUser().getUploadUrl())
        .leftPeople(mentoring.getNumberOfPeople() - userList.size())
        .followers(mentoring.getUser().getFollowerList().size())
        .isOwner(isOwner)
        .isMentoringLike(isMentoringLike)
        .isPrivateChatRoomCreate(isPrivateChatRoomCreate)
        .isMentorFollow(isMentorFollow)
        .registerDate(mentoring.getRegisterDate())
        .updateDate(mentoring.getUpdateDate())
        .build();
  }

}
