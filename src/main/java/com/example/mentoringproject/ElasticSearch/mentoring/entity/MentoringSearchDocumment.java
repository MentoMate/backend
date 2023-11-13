package com.example.mentoringproject.ElasticSearch.mentoring.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

  //ex) basic_date =  2023 01 11
  @Field(name = "product_created_at", type=FieldType.Date, format = DateFormat.basic_date)
  private LocalDate startDate; // 날짜를 문자열로 저장

  //ex) basic_date =  2023 01 11
  @Field(name = "product_created_at", type=FieldType.Date, format = DateFormat.basic_date)
  private LocalDate endDate; // 날짜를 문자열로 저장


  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;

  private Double rating;

  private String uploadUrl;
  private String uploadFolder;

  private int countWatch;

  public static MentoringSearchDocumment fromEntity(User user, Mentoring mentoring) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return MentoringSearchDocumment.builder()
        .id(mentoring.getId())
        .writer(user.getName())
        .title(mentoring.getTitle())
        .content(mentoring.getCategory())
        .startDate(mentoring.getStartDate())
        .endDate(mentoring.getEndDate())
        .numberOfPeople(mentoring.getNumberOfPeople())
        .amount(mentoring.getAmount())
        .status(mentoring.getStatus())
        .category(mentoring.getCategory())
        .rating(user.getRating())
        .uploadUrl(mentoring.getUploadUrl())
        .uploadFolder(mentoring.getUploadFolder())
        .countWatch(mentoring.getCountWatch())
        .build();
  }
}
