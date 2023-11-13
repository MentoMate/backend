package com.example.mentoringproject.user.user.entity;

import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

  @CreatedDate
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