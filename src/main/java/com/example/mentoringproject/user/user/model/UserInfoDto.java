package com.example.mentoringproject.user.user.model;

import com.example.mentoringproject.user.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
  private String name;
  private String email;
  private String nickname;

  public static UserInfoDto from(User user) {
    return UserInfoDto.builder()
        .name(user.getName())
        .email(user.getEmail())
        .nickname(user.getNickName())
        .build();
  }
}
