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
public class UserProfileList {

  private Long userId;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String uploadUrl;
  private String uploadFolder;
  private Double grade;

  public static Page<UserProfileList> from(Page<User> page){
   List<UserProfileList> userProfiles =  page.getContent().stream()
        .map(user -> UserProfileList.builder()
            .userId(user.getId())
            .name(user.getName())
            .career(user.getCareer())
            .introduce(user.getIntroduce())
            .mainCategory(user.getMainCategory())
            .middleCategory(user.getMiddleCategory())
            .grade(user.getRating())
            .build()
        ).collect(Collectors.toList());

    return new PageImpl<>(userProfiles, page.getPageable(), page.getTotalElements());
  }
}
