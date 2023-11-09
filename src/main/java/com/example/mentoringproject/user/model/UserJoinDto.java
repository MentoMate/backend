package com.example.mentoringproject.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserJoinDto {

  @Schema(description = "이메일", example = "abc@email.com")
  @Email
  @NotBlank(message = "이메일은 공백일 수 없습니다.")
  private String email;

  @Schema(description = "닉네임", minLength = 2, maxLength = 100, example = "nickName")
  @Length(min=2, max = 10, message = "닉네임의 길이는 최소 2자 최대10자 입니다.")
  private String nickName;

  @Schema(description = "비밀번호", minLength = 4, maxLength = 25, example = "1234")
  @Length(min=4, max = 25, message = "비밀번호는 최소 4자리 이상 25자리 이하입니다.")
  private String password;

}