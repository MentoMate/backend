package com.example.mentoringproject.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserJoinDto {

  private String email;
  private String password;
  private String nickName;
}