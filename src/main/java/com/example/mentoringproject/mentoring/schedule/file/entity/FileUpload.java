package com.example.mentoringproject.mentoring.schedule.file.entity;

import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import java.time.LocalDateTime;
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
@Entity(name = "file_schedule")
@EntityListeners(AuditingEntityListener.class)
public class FileUpload {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long id;

  private String fileName;
  private String uploadUrl;

  @ManyToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  @CreatedDate
  private LocalDateTime registerDate;


}
