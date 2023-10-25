package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.service.MentoringService;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
  private final MentoringService mentoringService;
  @PostMapping
  public ResponseEntity<?> createMentoring(
      @RequestHeader(value = "Authorization") String token,
      @RequestBody MentoringDto mentoringDto
  ) {
    mentoringService.createMentoring(token, mentoringDto);
    return ResponseEntity.ok("mentoring create success");
  }

  @PutMapping("/{mentoringId}")
  public ResponseEntity<?> updateMentoring(
      @PathVariable Long mentoringId,
      @RequestBody MentoringDto mentoringDto
  ) {
    mentoringService.updateMentoring(mentoringId, mentoringDto);
    return ResponseEntity.ok("mentoring update success");
  }

  @DeleteMapping("/{mentoringId}")
  public ResponseEntity<?> deleteMentoring(
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
