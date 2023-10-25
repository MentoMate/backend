package com.example.mentoringproject.mentoring.model;

import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MentoringDto {
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private MentoringStatus status;
  private String category;
  private String imgUrl;

}
