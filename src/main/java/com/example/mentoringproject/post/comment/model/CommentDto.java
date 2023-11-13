package com.example.mentoringproject.post.comment.model;

import com.example.mentoringproject.post.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentDto {

  private Long id;

  private String comment;

  private String nickName;
  private String userUploadUrl;
  private String userUploadFolder;

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;

  private boolean isOwner;

  public static CommentDto fromEntity(Comment comment) {
    return CommentDto.builder()
        .id(comment.getId())
        .comment(comment.getComment())
        .nickName(comment.getUser().getNickName())
        .userUploadUrl(comment.getUser().getUploadUrl())
        .userUploadFolder(comment.getUser().getUploadFolder())
        .registerDatetime(comment.getRegisterDatetime())
        .updateDatetime(comment.getUpdateDatetime())
        .isOwner(true)
        .build();
  }

  public static CommentDto fromEntity(Comment comment, boolean isOwner) {
    return CommentDto.builder()
            .id(comment.getId())
            .comment(comment.getComment())
            .nickName(comment.getUser().getNickName())
            .userUploadUrl(comment.getUser().getUploadUrl())
            .userUploadFolder(comment.getUser().getUploadFolder())
            .registerDatetime(comment.getRegisterDatetime())
            .updateDatetime(comment.getUpdateDatetime())
            .isOwner(isOwner)
            .build();
  }

}
