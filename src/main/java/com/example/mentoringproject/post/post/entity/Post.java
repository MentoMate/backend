package com.example.mentoringproject.post.post.entity;

import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.img.entity.Img;
import com.example.mentoringproject.post.post.model.PostRegisterDto;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.example.mentoringproject.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

  private LocalDateTime registerDatetime;
  private LocalDateTime updateDatetime;
  private LocalDateTime deleteDatetime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<Comment> comments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<PostLikes> postLikes = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  List<Img> imgs = new ArrayList<>();

  public static Post of(User user, PostRegisterDto postRegisterDto) {
    return Post.builder()
        .user(user)
        .category(postRegisterDto.getCategory())
        .title(postRegisterDto.getTitle())
        .content(postRegisterDto.getContent())
        .registerDatetime(LocalDateTime.now())
        .build();
  }

}
