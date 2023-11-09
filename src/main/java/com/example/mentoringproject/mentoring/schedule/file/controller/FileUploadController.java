package com.example.mentoringproject.mentoring.schedule.file.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.schedule.file.entity.FileUpload;
import com.example.mentoringproject.mentoring.schedule.file.service.FileUploadService;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/schedule/{scheduleId}/file")
public class FileUploadController {
  private  final FileUploadService fileUploadService;
  @PostMapping
  public ResponseEntity<List<FileUpload>> fileUpload(
      @PathVariable Long scheduleId,
      @RequestPart(name = "file") List<MultipartFile> multipartFileList
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(fileUploadService.fileUpload(email, scheduleId, multipartFileList));
  }

  @DeleteMapping("/{fileId}")
  public ResponseEntity<Void> fileDelete(
      @PathVariable Long fileId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    fileUploadService.fileDelete(email, fileId);
    return ResponseEntity.ok().build();
  }

}
