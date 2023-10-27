package com.example.mentoringproject.user.model;


import com.example.mentoringproject.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfile {

  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String imgUrl;

  public static UserProfile from(User user){
    return UserProfile.builder()
        .name(user.getName())
        .career(user.getCareer())
        .introduce(user.getIntroduce())
        .mainCategory(user.getMainCategory())
        .middleCategory(user.getMiddleCategory())
        .imgUrl(user.getImgUrl())
        .build();
  };
}
