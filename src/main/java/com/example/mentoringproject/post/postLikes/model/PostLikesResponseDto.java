package com.example.mentoringproject.post.postLikes.model;

import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostLikesResponseDto {

  private Long id;
  private LocalDateTime registerDatetime;
  private User user;
  private Post post;
  private boolean isLikes;

  public static PostLikesResponseDto fromEntity(PostLikes postLikes) {
    return PostLikesResponseDto.builder()
        .id(postLikes.getId())
        .registerDatetime(postLikes.getRegisterDatetime())
        .user(postLikes.getUser())
        .post(postLikes.getPost())
        .isLikes(true)
        .build();

  }
}
