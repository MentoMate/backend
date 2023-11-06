package com.example.mentoringproject.mentoring.schedule.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
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
@RequestMapping("/mentoring/{mentoringId}/schedule")
public class ScheduleController {
  private  final ScheduleService  scheduleService;
  @PostMapping
  public ResponseEntity<ScheduleInfo> createSchedule(
      @PathVariable Long mentoringId,
      @RequestBody ScheduleSave scheduleSave
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.createSchedule(email, mentoringId, scheduleSave)));
  }

  @PutMapping
  public ResponseEntity<ScheduleInfo> updateSchedule(
      @PathVariable Long mentoringId,
      @RequestBody ScheduleSave scheduleSave
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.updateSchedule(email, mentoringId, scheduleSave)));
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<Void> deleteSchedule(
          @PathVariable Long mentoringId,
          @PathVariable Long scheduleId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    scheduleService.deleteSchedule(email, mentoringId, scheduleId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{scheduleId}")
  public ResponseEntity<ScheduleInfo> scheduleInfo(
          @PathVariable Long mentoringId,
          @PathVariable Long scheduleId
  ){
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.scheduleInfo(scheduleId)));
  }

  @GetMapping
  public ResponseEntity<List<ScheduleInfo>> scheduleInfoByPeriod(
          @PathVariable Long mentoringId,
          @RequestParam("startDate") LocalDate startDate,
          @RequestParam("endDate") LocalDate endDate
  ){
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.scheduleInfoByPeriod(mentoringId, startDate, endDate)));
  }
}
