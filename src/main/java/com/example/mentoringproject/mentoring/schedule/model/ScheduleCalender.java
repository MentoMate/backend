
package com.example.mentoringproject.mentoring.schedule.model;

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
public class ScheduleCalender {

  private Long scheduleId;
  private String title;
  private String content;
  private LocalDate start;
  private String uploadFolder;
  private Long mentoringId;
  private Long userId;
  private boolean isMentor;
  private String backgroundColor;
  private String borderColor;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;
  public static List<ScheduleCalender> from(List<Schedule> scheduleList, boolean isMentor) {
    return scheduleList.stream()
            .map(schedule -> ScheduleCalender.builder()
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .content(schedule.getContent())
                    .start(schedule.getStart())
                    .uploadFolder(schedule.getUploadFolder())
                    .mentoringId(schedule.getMentoring().getId())
                    .userId(schedule.getMentoring().getUser().getId())
                    .isMentor(isMentor)
                    .backgroundColor(schedule.getBackgroundColor())
                    .borderColor(schedule.getBorderColor())
                    .registerDate(schedule.getRegisterDate())
                    .updateDate(schedule.getUpdateDate())
                    .build())
            .collect(Collectors.toList());
  }
}
