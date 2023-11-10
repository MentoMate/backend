package com.example.mentoringproject.mentoring.schedule.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.schedule.file.service.FileUploadService;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring/schedule")
public class ScheduleController {
  private final ScheduleService  scheduleService;
  private final FileUploadService fileUploadService;

  @PostMapping
  public ResponseEntity<ScheduleInfo> createSchedule(
      @RequestBody ScheduleSave scheduleSave
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.createSchedule(email, scheduleSave), fileUploadService.fileUploadList(scheduleSave.getScheduleId())));
  }

  @PutMapping
  public ResponseEntity<ScheduleInfo> updateSchedule(
      @RequestBody ScheduleSave scheduleSave
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.updateSchedule(email, scheduleSave), fileUploadService.fileUploadList(scheduleSave.getScheduleId())));
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<Void> deleteSchedule(
          @PathVariable Long scheduleId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    scheduleService.deleteSchedule(email, scheduleId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{scheduleId}")
  public ResponseEntity<ScheduleInfo> scheduleInfo(
          @PathVariable Long scheduleId
  ){
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.scheduleInfo(scheduleId), fileUploadService.fileUploadList(scheduleId)));
  }
}
