package com.example.mentoringproject.post.post.entity;

import com.example.mentoringproject.post.post.model.PostRegisterDto;
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
@Entity(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  private Category category;
  private String title;
  private String content;
  private String imgUrl;

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;
  private LocalDateTime deleteDatetime;

  @ManyToOne
  @JoinColumn(name = "user_Id")
  private User user;


  public static Post of(User user, PostRegisterDto postRegisterDto) {
    return Post.builder()
        .user(user)
        .category(postRegisterDto.getCategory())
        .title(postRegisterDto.getTitle())
        .content(postRegisterDto.getContent())
        .imgUrl(postRegisterDto.getImgUrl())
        .registerDatetime(LocalDateTime.now())
        .build();
  }

}
