package com.example.mentoringproject.mentoring.schedule.model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ScheduleSave {
  private Long id;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
}
