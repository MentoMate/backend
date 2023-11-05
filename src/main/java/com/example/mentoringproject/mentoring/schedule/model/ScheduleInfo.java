
package com.example.mentoringproject.mentoring.schedule.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleInfo {

  private Long id;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long mentoringId;
  private Long userId;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static ScheduleInfo from(Schedule schedule){
    return ScheduleInfo.builder()
        .id(schedule.getId())
        .title(schedule.getTitle())
        .content(schedule.getContent())
        .startDate(schedule.getStartDate())
        .endDate(schedule.getEndDate())
        .mentoringId(schedule.getMentoring().getId())
        .userId(schedule.getMentoring().getUser().getId())
        .registerDate(schedule.getRegisterDate())
        .updateDate(schedule.getUpdateDate())
        .build();
  }

}
