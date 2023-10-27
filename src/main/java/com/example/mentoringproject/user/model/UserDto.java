package com.example.mentoringproject.user.model;

import com.example.mentoringproject.user.entity.SocialType;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class UserDto {
  private Long id;
  private String nickName;
  private String email;
  private String emailAuth;
  private LocalDateTime emailAuthDate;
  private String socialId;
  private SocialType socialType;
  private LocalDateTime lastLogin;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String imgUrl;

  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  public static UserDto from(User user){
    return UserDto.builder()
        .id(user.getId())
        .nickName(user.getNickName())
        .email(user.getEmail())
        .emailAuth(user.getEmailAuth())
        .emailAuthDate(user.getEmailAuthDate())
        .socialId(user.getSocialId())
        .socialType(user.getSocialType())
        .lastLogin(user.getLastLogin())
        .name(user.getName())
        .career(user.getCareer())
        .introduce(user.getIntroduce())
        .mainCategory(user.getMainCategory())
        .middleCategory(user.getMiddleCategory())
        .imgUrl(user.getImgUrl())
        .registerDate(user.getRegisterDate())
        .updateDate(user.getUpdateDate())
        .deleteDate(user.getDeleteDate())
        .build();
  };
}
