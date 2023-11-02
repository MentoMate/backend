package com.example.mentoringproject.mentoring.img.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "img_mentoring")
@EntityListeners(AuditingEntityListener.class)
public class MentoringImg {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "img_id")
  private Long id;

  private String uploadPath;
  private String uploadName;
  private String uploadUrl;

  @CreatedDate
  private LocalDateTime registerDate;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoring;

}
