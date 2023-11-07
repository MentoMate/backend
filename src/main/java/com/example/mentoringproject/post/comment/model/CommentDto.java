package com.example.mentoringproject.post.comment.model;

import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentDto {

  private Long id;

  private String comment;

  private String nickName;
  private String uploadUrl;
  private String uploadFolder;
  private Post post;

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;

  public static List<CommentDto> fromEntity(Page<Comment> page) {
    return page.getContent().stream()
        .map(comment -> CommentDto.builder()
            .id(comment.getId())
            .comment(comment.getComment())
            .nickName(comment.getNickName())
            .uploadUrl(comment.getUploadUrl())
            .uploadFolder(comment.getUploadFolder())
            .post(comment.getPost())
            .registerDatetime(comment.getRegisterDatetime())
            .updateDatetime(comment.getUpdateDatetime())
            .build()
        )
        .collect(Collectors.toList());
  }

  public static CommentDto fromEntity(Comment comment) {
    return CommentDto.builder()
            .id(comment.getId())
            .comment(comment.getComment())
            .nickName(comment.getNickName())
            .uploadUrl(comment.getUploadUrl())
            .uploadFolder(comment.getUploadFolder())
            .post(comment.getPost())
            .registerDatetime(comment.getRegisterDatetime())
            .updateDatetime(comment.getUpdateDatetime())
            .build();
  }

}
