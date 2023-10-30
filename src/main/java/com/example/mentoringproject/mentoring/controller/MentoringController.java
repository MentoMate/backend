package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.service.MentoringService;
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
@RequestMapping("/mentoring")
public class MentoringController {
  private final MentoringService mentoringService;
  private final S3Service s3Service;

  @PostMapping
  public ResponseEntity<String> createMentoring(
      @RequestPart MentoringDto mentoringDto,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFiles
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    mentoringService.createMentoring(email, mentoringDto, multipartFiles);
    return ResponseEntity.ok("mentoring create success");
  }

  @PutMapping("/{mentoringId}")
  public ResponseEntity<String> updateMentoring(
      @PathVariable Long mentoringId,
      @RequestBody MentoringDto mentoringDto
  ) {
    mentoringService.updateMentoring(mentoringId, mentoringDto);
    return ResponseEntity.ok("mentoring update success");
  }

  @DeleteMapping("/{mentoringId}")
  public ResponseEntity<String> deleteMentoring(
      @PathVariable Long mentoringId
  ) {
    mentoringService.deleteMentoring(mentoringId);
    return ResponseEntity.ok("mentoring delete success");
  }

  @GetMapping("/{mentoringId}")
  public ResponseEntity<MentoringInfo> getMentoring(
      @PathVariable Long mentoringId
  ) {
    return ResponseEntity.ok(mentoringService.MentoringInfo(mentoringId));
  }

}
