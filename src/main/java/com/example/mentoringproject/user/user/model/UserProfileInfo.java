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
public class UserProfileInfo {

  private Long userId;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String uploadUrl;
  private String uploadFolder;
  private boolean isMentorFollow;
  private int followers;

  public static UserProfileInfo from(User user, boolean isMentorFollow){
    return UserProfileInfo.builder()
        .userId(user.getId())
        .name(user.getName())
        .career(user.getCareer())
        .introduce(user.getIntroduce())
        .mainCategory(user.getMainCategory())
        .middleCategory(user.getMiddleCategory())
        .uploadFolder(user.getUploadFolder())
        .uploadUrl(user.getUploadUrl())
        .followers(user.getFollowerList().size())
        .isMentorFollow(isMentorFollow)
        .build();
  }

}
