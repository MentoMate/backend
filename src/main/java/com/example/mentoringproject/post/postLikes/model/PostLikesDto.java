package com.example.mentoringproject.post.postLikes.model;

import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostLikesDto {
  private Long id;
  private LocalDateTime registerDatetime;

  public static PostLikesDto fromEntity(PostLikes postLikes){
    return PostLikesDto.builder()
        .id(postLikes.getId())
        .registerDatetime(postLikes.getRegisterDatetime())
        .build();
  }
}
