package com.example.mentoringproject.mentee.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.entity.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


}
