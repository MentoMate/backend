package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.mentee.entity.Mentee;
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
public class GradeResponseDto {
  private Long mentoringId;
  private Long userId;
  private String comment;
  private Integer rating;

  public static GradeResponseDto from(Mentee mentee, User user) {
    return GradeResponseDto.builder()
        .mentoringId(mentee.getMentoring().getId())
        .userId(user.getId())
        .comment(mentee.getComment())
        .rating(mentee.getRating())
        .build();
  }
}
