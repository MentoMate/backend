package com.example.mentoringproject.post.post.model;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostMyPageResponse {
  private Long id;
  private Category category;
  private String title;
  private String content;
  private String writer;
  private String uploadUrl;
  private String uploadFolder;
  private int postLikesCount;
  private int commentCount;

  public static PostMyPageResponse fromEntity(Post post) {
    return PostMyPageResponse.builder()
        .id(post.getId())
        .category(post.getCategory())
        .title(post.getTitle())
        .content(post.getContent())
        .writer(post.getUser().getNickName())
        .uploadUrl(post.getUploadUrl())
        .uploadFolder(post.getUploadFolder())
        .postLikesCount(post.getPostLikesCount())
        .commentCount(post.getCommentCount())
        .build();
  }
}
