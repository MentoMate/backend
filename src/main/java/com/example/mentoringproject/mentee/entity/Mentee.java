package com.example.mentoringproject.mentee.entity;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Mentee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoring;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String comment;

  private Integer rating;

  @CreatedDate
  private LocalDateTime registerDate;

  @LastModifiedDate
  private LocalDateTime updateDate;
}
