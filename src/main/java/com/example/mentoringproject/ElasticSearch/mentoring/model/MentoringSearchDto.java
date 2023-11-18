package com.example.mentoringproject.ElasticSearch.mentoring.model;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
  private boolean isMentorRegister;

  public static MentoringSearchDto fromDocument(
      MentoringSearchDocumment mentoringSearchDocumment, boolean isMentorRegister, MentoringRepository mentoringRepository) {
    Mentoring mentoring = mentoringRepository.findById(mentoringSearchDocumment.getId()).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST,"Not found Mentoring"));
    User user = mentoring.getUser();
    Double rating = user != null ? user.getRating() : 0.0;
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
        .rating(rating)
        .uploadUrl(mentoringSearchDocumment.getUploadUrl())
        .uploadFolder(mentoringSearchDocumment.getUploadFolder())
        .isMentorRegister(isMentorRegister)
        .build();
  }
}
