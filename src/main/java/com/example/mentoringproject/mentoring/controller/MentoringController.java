package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.model.MentoringList;
import com.example.mentoringproject.mentoring.service.MentoringService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
  private final MentoringService mentoringService;

  @PostMapping
  public ResponseEntity<MentoringDto> createMentoring(
      @RequestPart MentoringSave mentoringSave,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.createMentoring(email, mentoringSave, thumbNailImg)));
  }

  @PutMapping
  public ResponseEntity<MentoringDto> updateMentoring(
      @RequestPart MentoringSave mentoringSave,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg
  ) {

    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.updateMentoring(email, mentoringSave, thumbNailImg)));
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

  @GetMapping
  public ResponseEntity<Page<MentoringList>> getMentoringList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "5") int pageSize,
          @RequestParam(defaultValue = "id") String sortId,
          @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);

    return ResponseEntity.ok(MentoringList.from(mentoringService.getMentoringList(pageable)));
}
  @GetMapping("/main")
  public ResponseEntity<Map<String, List<?>>> getMentoringMain() {
    Map<String, List<?>> mentoringMainPageDtoMap = new HashMap<>();
    mentoringMainPageDtoMap.put("MentoringByCountWatch", mentoringService.getMentoringByCountWatch());
    mentoringMainPageDtoMap.put("MentorByRating", mentoringService.getMentorByRating());
    mentoringMainPageDtoMap.put("PostRegisterDateTime", mentoringService.getPostByRegisterDateTime());
    mentoringMainPageDtoMap.put("MentoringByEndDate", mentoringService.getMentoringByEndDate());
    mentoringMainPageDtoMap.put("Count",mentoringService.getCount());
    return ResponseEntity.ok(mentoringMainPageDtoMap);
  }
}
