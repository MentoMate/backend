package com.example.mentoringproject.mentoring.schedule.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring/{mentoringId/schedule")
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
      @PathVariable Long scheduleId
  ) {
    scheduleService.deleteSchedule(scheduleId);
    return ResponseEntity.ok().build();
  }
}
