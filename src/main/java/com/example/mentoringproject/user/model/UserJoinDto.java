package com.example.mentoringproject.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserJoinDto {

  @Schema(description = "이메일", nullable = false, example = "abc@email.com")
  private String email;
  private String password;
  private String nickName;

}