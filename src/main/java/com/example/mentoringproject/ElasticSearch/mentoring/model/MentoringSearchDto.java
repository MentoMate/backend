package com.example.mentoringproject.ElasticSearch.mentoring.model;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MentoringSearchDto {

  private Long id;
  private String writer;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;

  private Double rating;

  private String uploadUrl;
  private String uploadFolder;

  private int countWatch;

  private boolean isMentorRegister;

  public static MentoringSearchDto fromDocument(
      MentoringSearchDocumment mentoringSearchDocumment, boolean isMentorRegister) {
    return MentoringSearchDto.builder()
        .id(mentoringSearchDocumment.getId())
        .writer(mentoringSearchDocumment.getWriter())
        .title(mentoringSearchDocumment.getTitle())
        .content(mentoringSearchDocumment.getContent())
        .startDate(mentoringSearchDocumment.getStartDate())
        .endDate(mentoringSearchDocumment.getEndDate())
        .numberOfPeople(mentoringSearchDocumment.getNumberOfPeople())
        .amount(mentoringSearchDocumment.getAmount())
        .status(mentoringSearchDocumment.getStatus())
        .category(mentoringSearchDocumment.getCategory())
        .rating(mentoringSearchDocumment.getRating())
        .uploadUrl(mentoringSearchDocumment.getUploadUrl())
        .uploadFolder(mentoringSearchDocumment.getUploadFolder())
        .isMentorRegister(isMentorRegister)
        .build();
  }
}
