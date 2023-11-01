package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.service.MentoringService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
  private final MentoringService mentoringService;
  @PostMapping
  public ResponseEntity<String> createMentoring(
      @RequestBody MentoringDto mentoringDto
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    mentoringService.createMentoring(email, mentoringDto);
    return ResponseEntity.ok("mentoring create success");
  }

  @PutMapping("/{mentoringId}")
  public ResponseEntity<String> updateMentoring(
      @PathVariable Long mentoringId,
      @RequestBody MentoringDto mentoringDto
  ) {

    String email = SpringSecurityUtil.getLoginEmail();
    mentoringService.updateMentoring(email, mentoringId, mentoringDto);
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

  @GetMapping("/main")
  public ResponseEntity<Map<String, List<?>>> getMentoringMain() {
    Map<String, List<?>> mentoringMainPageDtoMap = new HashMap<>();
    mentoringMainPageDtoMap.put("MentoringByCountWatch", mentoringService.getMentoringByCountWatch());
    mentoringMainPageDtoMap.put("MentorByRating", mentoringService.getMentorByRating());
    mentoringMainPageDtoMap.put("PostRegisterDateTime", mentoringService.getPostByRegisterDateTime());
    mentoringMainPageDtoMap.put("MentoringByEndDate", mentoringService.getMentoringByEndDate());
    return ResponseEntity.ok(mentoringMainPageDtoMap);
  }
}
