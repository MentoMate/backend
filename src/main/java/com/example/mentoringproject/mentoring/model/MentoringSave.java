package com.example.mentoringproject.mentoring.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;


@Getter
public class MentoringSave {
  private Long mentoringId;
  private String title;
  private String content;
  private LocalDate startDate;
  private LocalDate endDate;
  private int numberOfPeople;
  private int amount;
  private String category;
  private String uploadFolder;
  private List<String> uploadImg;
}
