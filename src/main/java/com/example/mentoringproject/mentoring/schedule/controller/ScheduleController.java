package com.example.mentoringproject.mentoring.schedule.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.schedule.file.service.FileUploadService;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleDetailInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "일정", description = "일정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring/schedule")
public class ScheduleController {
  private final ScheduleService  scheduleService;
  private final FileUploadService fileUploadService;
  @Operation(summary = "일정 등록 api", description = "일정 등록 api", responses = {
      @ApiResponse(responseCode = "200", description = "일정 등록 성공", content =
      @Content(schema = @Schema(implementation = ScheduleInfo.class)))
  })
  @PostMapping
  public ResponseEntity<ScheduleInfo> createSchedule(
      @RequestBody ScheduleSave scheduleSave
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.createSchedule(email, scheduleSave)));
  }
  @Operation(summary = "일정 수정 api", description = "일정 수정 api", responses = {
      @ApiResponse(responseCode = "200", description = "일정 수정 성공", content =
      @Content(schema = @Schema(implementation = ScheduleDetailInfo.class)))
  })
  @PutMapping
  public ResponseEntity<ScheduleDetailInfo> updateSchedule(
      @RequestBody ScheduleSave scheduleSave
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ScheduleDetailInfo.from(scheduleService.updateSchedule(email, scheduleSave), fileUploadService.fileUploadList(scheduleSave.getScheduleId())));
  }

  @Operation(summary = "일정 삭제 api", description = "일정 삭제 api", responses = {
      @ApiResponse(responseCode = "200", description = "일정 삭제 성공")
  })
  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<Void> deleteSchedule(
          @PathVariable Long scheduleId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    scheduleService.deleteSchedule(email, scheduleId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "일정 조회 api", description = "일정 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "일정 조회 성공", content =
      @Content(schema = @Schema(implementation = ScheduleDetailInfo.class)))
  })
  @GetMapping("/{scheduleId}")
  public ResponseEntity<ScheduleDetailInfo> scheduleInfo(
          @PathVariable Long scheduleId
  ){
    return ResponseEntity.ok(ScheduleDetailInfo.from(scheduleService.scheduleInfo(scheduleId), fileUploadService.fileUploadList(scheduleId)));
  }
}
