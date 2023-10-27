package com.example.mentoringproject.post.comment.model;

import com.example.mentoringproject.post.comment.entity.Comment;
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

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;
  private LocalDateTime deleteDatetime;

  public static List<CommentDto> fromEntity(Page<Comment> page) {
    return page.getContent().stream()
        .map(comment -> CommentDto.builder()
            .id(comment.getId())
            .comment(comment.getComment())
            .registerDatetime(comment.getRegisterDatetime())
            .updateDatetime(comment.getUpdateDatetime())
            .build()
        )
        .collect(Collectors.toList());
  }

}
