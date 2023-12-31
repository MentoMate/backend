package com.example.mentoringproject.mentoring.mentoring.model;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentoringByEndDateDto {

  private Long mentoringId;
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

  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  private Double rating;
  private String name;

  public static List<MentoringByEndDateDto> fromEntity(List<Mentoring> mentoringList) {
    return mentoringList.stream()
        .map(mentoring -> MentoringByEndDateDto.builder()
            .mentoringId(mentoring.getId())
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
            .countWatch(mentoring.getCountWatch())
            .registerDate(mentoring.getRegisterDate())
            .updateDate(mentoring.getUpdateDate())
            .deleteDate(mentoring.getDeleteDate())
            .rating(mentoring.getUser().getRating())
            .name(mentoring.getUser().getName())
            .build())
        .collect(Collectors.toList());
  }

}
