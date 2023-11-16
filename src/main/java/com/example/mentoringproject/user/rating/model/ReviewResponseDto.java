package com.example.mentoringproject.user.rating.model;

import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDateTime;
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
public class ReviewResponseDto {
  private Long mentoringId;
  private Long userId;
  private String comment;
  private Integer rating;
  private LocalDateTime updateDate;

  public static ReviewResponseDto from(Mentee mentee, User user) {
    return ReviewResponseDto.builder()
        .mentoringId(mentee.getMentoring().getId())
        .userId(user.getId())
        .comment(mentee.getComment())
        .rating(mentee.getRating())
        .updateDate(mentee.getUpdateDate())
        .build();
  }

  public static ReviewResponseDto from(Mentee mentee) {
    return ReviewResponseDto.builder()
        .mentoringId(mentee.getMentoring().getId())
        .userId(mentee.getUser().getId())
        .comment(mentee.getComment())
        .rating(mentee.getRating())
        .updateDate(mentee.getUpdateDate())
        .build();
  }
}
