package com.example.mentoringproject.post.comment.entity;

import com.example.mentoringproject.post.comment.model.CommentRegisterDto;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  private String comment;

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;
  private LocalDateTime deleteDatetime;

  @ManyToOne
  @JoinColumn(name = "user_Id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_Id")
  private Post post;


  public static Comment of(User user, CommentRegisterDto commentRegisterDto) {
    return Comment.builder()
        .user(user)
        .comment(commentRegisterDto.getComment())
        .registerDatetime(LocalDateTime.now())
        .build();
  }

}
