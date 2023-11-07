
package com.example.mentoringproject.mentoring.schedule.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

  private Long scheduleId;
  private String title;
  private String content;
  private LocalDate startDate;
  private String uploadFolder;
  private Long mentoringId;
  private Long userId;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static ScheduleInfo from(Schedule schedule){
    return ScheduleInfo.builder()
        .scheduleId(schedule.getId())
        .title(schedule.getTitle())
        .content(schedule.getContent())
        .startDate(schedule.getStartDate())
        .uploadFolder(schedule.getUploadFolder())
        .mentoringId(schedule.getMentoring().getId())
        .userId(schedule.getMentoring().getUser().getId())
        .registerDate(schedule.getRegisterDate())
        .updateDate(schedule.getUpdateDate())
        .build();
  }

  public static List<ScheduleInfo> from(List<Schedule> scheduleList) {
    return scheduleList.stream()
            .map(schedule -> ScheduleInfo.builder()
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .content(schedule.getContent())
                    .startDate(schedule.getStartDate())
                    .uploadFolder(schedule.getUploadFolder())
                    .mentoringId(schedule.getMentoring().getId())
                    .userId(schedule.getMentoring().getUser().getId())
                    .registerDate(schedule.getRegisterDate())
                    .updateDate(schedule.getUpdateDate())
                    .build())
            .collect(Collectors.toList());
  }
}
