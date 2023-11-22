package com.example.mentoringproject.ElasticSearch.mentor.entity;

import com.example.mentoringproject.user.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "mentor")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MentorSearchDocumment {

  @Id
  private Long id;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;

  private String uploadUrl;
  private String uploadFolder;

  private Double rating;

  public static MentorSearchDocumment fromEntity(User user) {
    return MentorSearchDocumment.builder()
        .id(user.getId())
        .name(user.getName())
        .career(user.getCareer())
        .introduce(user.getIntroduce())
        .mainCategory(user.getMainCategory())
        .middleCategory(user.getMiddleCategory())
        .uploadUrl(user.getUploadUrl())
        .uploadFolder(user.getUploadFolder())
        .rating(user.getRating())
        .build();
  }

}
