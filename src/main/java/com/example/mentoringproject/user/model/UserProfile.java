package com.example.mentoringproject.user.model;


import com.example.mentoringproject.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Getter
@Builder
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
        .followers(1)
        .build();
  }

}
