package com.example.mentoringproject.mentoring.schedule.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class ScheduleSave {
  private Long scheduleId;
  private Long mentoringId;
  private String title;
  private String content;
  private LocalDate start;
  private String uploadFolder;
  private List<String> uploadImg;
}
