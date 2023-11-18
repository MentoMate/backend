package com.example.mentoringproject.mentoring.mentoring.model;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MentoringDto {
  private Long id;
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
  private Long userId;
  private int countWatch;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static MentoringDto from(Mentoring mentoring) {
    return MentoringDto.builder()
        .id(mentoring.getId())
        .title(mentoring.getTitle())
        .content(mentoring.getContent())
        .startDate(mentoring.getStartDate())
        .endDate(mentoring.getEndDate())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .uploadUrl(mentoring.getUploadUrl())
        .uploadFolder(mentoring.getUploadFolder())
        .userId(mentoring.getUser().getId())
        .countWatch(mentoring.getCountWatch())
        .registerDate(mentoring.getRegisterDate())
        .updateDate(mentoring.getUpdateDate())
        .deleteDate(mentoring.getDeleteDate())
        .build();
  }
}
