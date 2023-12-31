package com.example.mentoringproject.post.post.model;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostInfoResponseDto {

  private Long id;

  private Category category;
  private String title;
  private String content;
  private String uploadUrl;
  private String uploadFolder;
  private int postLikesCount;
  private int commentCount;
  private int countWatch;
  private String nickName;
  private boolean isOwner;
  private boolean isLike;
  private String UserUploadUrl;
  private String UserUploadFolder;

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;

  public static PostInfoResponseDto fromEntity(Post post, boolean isOwner, boolean isLike) {
    return PostInfoResponseDto.builder()
        .id(post.getId())
        .category(post.getCategory())
        .title(post.getTitle())
        .content(post.getContent())
        .uploadUrl(post.getUploadUrl())
        .uploadFolder(post.getUploadFolder())
        .postLikesCount(post.getPostLikesCount())
        .commentCount(post.getCommentCount())
        .countWatch(post.getCountWatch())
        .nickName(post.getUser().getNickName())
        .isOwner(isOwner)
        .isLike(isLike)
        .registerDatetime(post.getRegisterDatetime())
        .updateDatetime(post.getUpdateDatetime())
        .UserUploadUrl(post.getUser().getUploadUrl())
        .UserUploadFolder(post.getUser().getUploadFolder())
        .build();
  }

}
