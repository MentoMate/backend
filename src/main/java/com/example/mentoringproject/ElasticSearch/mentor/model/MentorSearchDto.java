package com.example.mentoringproject.ElasticSearch.mentor.model;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MentorSearchDto {

  private Long id;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;

  private String uploadUrl;
  private String uploadFolder;

  private Double rating;

  private boolean isMentorRegister;

  public static MentorSearchDto fromDocument(MentorSearchDocumment mentorSearchDocumment, boolean isMentorRegister) {
    return MentorSearchDto.builder()
        .id(mentorSearchDocumment.getId())
        .name(mentorSearchDocumment.getName())
        .career(mentorSearchDocumment.getCareer())
        .introduce(mentorSearchDocumment.getIntroduce())
        .mainCategory(mentorSearchDocumment.getMainCategory())
        .middleCategory(mentorSearchDocumment.getMiddleCategory())
        .uploadUrl(mentorSearchDocumment.getUploadUrl())
        .uploadFolder(mentorSearchDocumment.getUploadFolder())
        .rating(mentorSearchDocumment.getRating())
        .isMentorRegister(isMentorRegister)
        .build();
  }

}
