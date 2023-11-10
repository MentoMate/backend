
package com.example.mentoringproject.mentoring.schedule.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class ScheduleDto {

  private Long scheduleId;
  private String title;
  private String content;
  private LocalDate start;
  private Mentoring mentoring;
  private String uploadFolder;
  private String backgroundColor;
  private String borderColor;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static ScheduleDto from(Schedule schedule){
    return ScheduleDto.builder()
        .scheduleId(schedule.getId())
        .title(schedule.getTitle())
        .content(schedule.getContent())
        .start(schedule.getStart())
        .uploadFolder(schedule.getUploadFolder())
        .mentoring(schedule.getMentoring())
        .backgroundColor(schedule.getBackgroundColor())
        .borderColor(schedule.getBorderColor())
        .registerDate(schedule.getRegisterDate())
        .updateDate(schedule.getUpdateDate())
        .build();
  }

}
