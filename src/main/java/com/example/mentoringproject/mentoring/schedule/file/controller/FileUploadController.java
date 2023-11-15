package com.example.mentoringproject.mentoring.schedule.file.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.schedule.file.entity.FileUpload;
import com.example.mentoringproject.mentoring.schedule.file.model.FileUploadInfo;
import com.example.mentoringproject.mentoring.schedule.file.service.FileUploadService;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "일정 파일 업로드", description = "일정 파일 업로드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule/{scheduleId}/file")
public class FileUploadController {
  private  final FileUploadService fileUploadService;

  @Operation(summary = "파일 업로드 api", description = "파일 업로드 api", responses = {
      @ApiResponse(responseCode = "200", description = "파일 업로드 성공", content =
      @Content(schema = @Schema(implementation = ScheduleInfo.class)))
  })
  @PostMapping
  public ResponseEntity<List<FileUploadInfo>> fileUpload(
      @PathVariable Long scheduleId,
      @RequestPart(name = "file") List<MultipartFile> multipartFileList
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(FileUploadInfo.from(fileUploadService.fileUpload(email, scheduleId, multipartFileList)));
  }

  @Operation(summary = "파일 삭제 api", description = "파일 삭제 api", responses = {
      @ApiResponse(responseCode = "200", description = "파일 삭제 성공")
  })
  @DeleteMapping("/{fileId}")
  public ResponseEntity<Void> fileDelete(
      @PathVariable Long fileId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    fileUploadService.fileDelete(email, fileId);
    return ResponseEntity.ok().build();
  }

}
