
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

  private Long id;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private Mentoring mentoring;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static ScheduleDto from(Schedule schedule){
    return ScheduleDto.builder()
        .id(schedule.getId())
        .title(schedule.getTitle())
        .content(schedule.getContent())
        .startDate(schedule.getStartDate())
        .endDate(schedule.getEndDate())
        .mentoring(schedule.getMentoring())
        .registerDate(schedule.getRegisterDate())
        .updateDate(schedule.getUpdateDate())
        .build();
  }

}
