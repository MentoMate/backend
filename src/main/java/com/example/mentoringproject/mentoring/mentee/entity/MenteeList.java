package com.example.mentoringproject.mentoring.mentee.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.entity.User;
import javax.persistence.Column;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "mentee_list")
@EntityListeners(AuditingEntityListener.class)
public class MenteeList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mentee_list_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoringId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
