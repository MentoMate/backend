
package com.example.mentoringproject.mentoring.schedule.model;

import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.file.entity.FileUpload;
import com.example.mentoringproject.mentoring.file.model.FileUploadInfo;
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
public class ScheduleDetailInfo {

  private Long scheduleId;
  private String title;
  private String content;
  private LocalDate start;
  private String uploadFolder;
  private Long mentoringId;
  private Long userId;
  private List<FileUploadInfo> fileUploadList;
  private String backgroundColor;
  private String borderColor;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static ScheduleDetailInfo from(Schedule schedule, List<FileUpload> fileUploadList){
    return ScheduleDetailInfo.builder()
        .scheduleId(schedule.getId())
        .title(schedule.getTitle())
        .content(schedule.getContent())
        .start(schedule.getStart())
        .uploadFolder(schedule.getUploadFolder())
        .mentoringId(schedule.getMentoring().getId())
        .fileUploadList(FileUploadInfo.from(fileUploadList))
        .backgroundColor(schedule.getBackgroundColor())
        .borderColor(schedule.getBorderColor())
        .userId(schedule.getMentoring().getUser().getId())
        .registerDate(schedule.getRegisterDate())
        .updateDate(schedule.getUpdateDate())
        .build();
  }

  public static List<ScheduleDetailInfo> from(List<Schedule> scheduleList, List<FileUpload> fileUploadList) {
    return scheduleList.stream()
            .map(schedule -> ScheduleDetailInfo.builder()
                    .scheduleId(schedule.getId())
                    .title(schedule.getTitle())
                    .content(schedule.getContent())
                    .start(schedule.getStart())
                    .uploadFolder(schedule.getUploadFolder())
                    .mentoringId(schedule.getMentoring().getId())
                    .fileUploadList(FileUploadInfo.from(fileUploadList))
                    .backgroundColor(schedule.getBackgroundColor())
                    .borderColor(schedule.getBorderColor())
                    .userId(schedule.getMentoring().getUser().getId())
                    .registerDate(schedule.getRegisterDate())
                    .updateDate(schedule.getUpdateDate())
                    .build())
            .collect(Collectors.toList());
  }
}
