package com.example.mentoringproject.post.post.model;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostByRegisterDateDto {
  private Long postId;
  private Category category;
  private String title;
  private String content;
  private String uploadUrl;
  private String uploadFolder;
  private int postLikesCount;
  private int commentCount;
  private int countWatch;
  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;
  private String writer;


  public static List<PostByRegisterDateDto> fromEntity(List<Post> postList) {
    return postList.stream()
        .map(post -> PostByRegisterDateDto.builder()
            .postId(post.getId())
            .category(post.getCategory())
            .title(post.getTitle())
            .content(post.getContent())
            .uploadUrl(post.getUploadUrl())
            .uploadFolder(post.getUploadFolder())
            .postLikesCount(post.getPostLikesCount())
            .commentCount(post.getCommentCount())
            .countWatch(post.getCountWatch())
            .registerDatetime(post.getRegisterDatetime())
            .updateDatetime(post.getUpdateDatetime())
            .writer(post.getUser().getNickName())
            .build())
        .collect(Collectors.toList());
  }


}
