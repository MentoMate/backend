package com.example.mentoringproject.user.user.model;


import com.example.mentoringproject.user.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

  private Long userId;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String uploadUrl;
  private String uploadFolder;
  private int followers;

  public static UserProfile from(User user){
    return UserProfile.builder()
        .userId(user.getId())
        .name(user.getName())
        .career(user.getCareer())
        .introduce(user.getIntroduce())
        .mainCategory(user.getMainCategory())
        .middleCategory(user.getMiddleCategory())
        .uploadFolder(user.getUploadFolder())
        .uploadUrl(user.getUploadUrl())
        .followers(user.getFollowerList().size())
        .build();
  }

}
