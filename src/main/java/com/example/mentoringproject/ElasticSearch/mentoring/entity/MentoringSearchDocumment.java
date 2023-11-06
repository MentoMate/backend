package com.example.mentoringproject.ElasticSearch.mentoring.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "mentoring")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentoringSearchDocumment {

  @Id
  private Long id;
  private String writer;
  private String title;
  private String content;
  //private LocalDate startDate;
  //private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;

  private Double rating;

  private String uploadUrl;
  private String uploadFolder;

  private int countWatch;

  public static MentoringSearchDocumment fromEntity(User user, Mentoring mentoring) {
    System.out.println(mentoring.getId());
    return MentoringSearchDocumment.builder()
        .id(mentoring.getId())
        .writer(user.getName())
        .title(mentoring.getTitle())
        .content(mentoring.getCategory())
        //.startDate(mentoring.getStartDate())
        //.endDate(mentoring.getEndDate())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .rating(user.getRating())
        .uploadUrl(mentoring.getUploadUrl())
        .uploadFolder(mentoring.getUploadUrl())
        .countWatch(mentoring.getCountWatch())
        .build();
  }
}
