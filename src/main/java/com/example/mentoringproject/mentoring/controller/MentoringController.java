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

  @PostMapping
  public ResponseEntity<MentoringDto> createMentoring(
      @RequestPart MentoringDto mentoringDto,
      @RequestPart(name = "img", required = false) List<MultipartFile> multipartFiles
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.createMentoring(email, mentoringDto, multipartFiles)));
  }

  @PutMapping("/{mentoringId}")
  public ResponseEntity<MentoringDto> updateMentoring(
      @PathVariable Long mentoringId,
      @RequestBody MentoringDto mentoringDto
  ) {
    return ResponseEntity.ok(MentoringDto.from(mentoringService.updateMentoring(mentoringId, mentoringDto)));
  }

  @DeleteMapping("/{mentoringId}")
  public ResponseEntity<Void> deleteMentoring(
      @PathVariable Long mentoringId
  ) {
    mentoringService.deleteMentoring(mentoringId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{mentoringId}")
  public ResponseEntity<MentoringInfo> MentoringInfo(
      @PathVariable Long mentoringId
  ) {
    return ResponseEntity.ok(MentoringInfo.from(mentoringService.mentoringInfo(mentoringId)));
  }

}
