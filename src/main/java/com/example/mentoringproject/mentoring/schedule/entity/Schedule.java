package com.example.mentoringproject.mentoring.schedule.entity;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.schedule.file.entity.FileUpload;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@Entity(name = "schedule")
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_id")
  private Long id;

  private String title;
  private String content;
  private LocalDate start;
  private String uploadFolder;
  private String backgroundColor;
  private String borderColor;

  @ManyToOne
  @JoinColumn(name = "mentoring_id")
  private Mentoring mentoring;

  @OneToMany
  private List<FileUpload> fileUploadList = new ArrayList<>();

  @CreatedDate
  private LocalDateTime registerDate;
  @LastModifiedDate
  private LocalDateTime updateDate;

  public static Schedule from(ScheduleSave scheduleSave, Mentoring mentoring){
    return Schedule.builder()
        .title(scheduleSave.getTitle())
        .content(scheduleSave.getContent())
        .start(scheduleSave.getStart())
        .mentoring(mentoring)
        .backgroundColor("#ABDEE6")
        .borderColor("#ABDEE6")
        .uploadFolder(scheduleSave.getUploadFolder())
        .build();
  }

}
