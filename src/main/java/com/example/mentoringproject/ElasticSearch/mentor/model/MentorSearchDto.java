package com.example.mentoringproject.ElasticSearch.mentor.model;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

  private int followers;

  public static MentorSearchDto fromDocument(MentorSearchDocumment mentorSearchDocumment,
      boolean isMentorRegister, UserRepository userRepository) {
    User user = userRepository.findById(mentorSearchDocumment.getId()).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST,"Not found User"));
    int followersCount = user != null && user.getFollowerList() != null ? user.getFollowerList().size() : 0;
    Double rating = user != null ? user.getRating() : 0.0;
    return MentorSearchDto.builder()
        .id(mentorSearchDocumment.getId())
        .name(mentorSearchDocumment.getName())
        .career(mentorSearchDocumment.getCareer())
        .introduce(mentorSearchDocumment.getIntroduce())
        .mainCategory(mentorSearchDocumment.getMainCategory())
        .middleCategory(mentorSearchDocumment.getMiddleCategory())
        .uploadUrl(mentorSearchDocumment.getUploadUrl())
        .uploadFolder(mentorSearchDocumment.getUploadFolder())
        .rating(rating)
        .isMentorRegister(isMentorRegister)
        .followers(followersCount)
        .build();
  }
}
