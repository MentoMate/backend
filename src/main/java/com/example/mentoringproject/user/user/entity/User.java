package com.example.mentoringproject.user.user.entity;

import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  private String nickName;

  private String email;
  private String emailAuth;
  private LocalDateTime emailAuthDate;
  private String password;

  private String socialId;
  @Enumerated(EnumType.STRING)
  private SocialType socialType;
  private String refreshToken;
  private LocalDateTime lastLogin;

  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;

  private String uploadUrl;
  private String uploadFolder;

  private Double rating; // 평점

  private LocalDateTime registerDate;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private LocalDateTime deleteDate;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<Post> posts = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<Comment> comments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<PostLikes> postLikes = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "users_follower",
      joinColumns = @JoinColumn(name = "mentor_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> followerList = new ArrayList<>();

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }
}