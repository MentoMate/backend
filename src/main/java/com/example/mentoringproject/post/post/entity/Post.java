package com.example.mentoringproject.post.post.entity;

import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.example.mentoringproject.user.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private Category category;
  private String title;
  private String content;
  private String uploadUrl;
  private String uploadFolder;

  private int postLikesCount;

  private int commentCount;

  private int countWatch;

  @CreatedDate
  private LocalDateTime registerDatetime;
  @LastModifiedDate
  private LocalDateTime updateDatetime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
  List<Comment> comments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
  List<PostLikes> postLikes = new ArrayList<>();


  public static Post from (User user, PostRegisterRequest postRegisterRequest) {
    return Post.builder()
        .user(user)
        .category(postRegisterRequest.getCategory())
        .title(postRegisterRequest.getTitle())
        .content(postRegisterRequest.getContent())
        .uploadFolder(postRegisterRequest.getUploadFolder())
        .postLikesCount(0)
        .commentCount(0)
        .countWatch(0)
        .build();
  }

}
