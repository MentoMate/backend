package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.model.MentoringList;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.user.model.UserProfileList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "멘토링", description = "멘토링 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mentoring")
public class MentoringController {
  private final MentoringService mentoringService;
  private final ScheduleService scheduleService;

  @Operation(summary = "멘토링 등록 api", description = "멘토링 등록 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 등록 성공", content =
      @Content(schema = @Schema(implementation = MentoringDto.class)))
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MentoringDto> createMentoring(
      @RequestPart MentoringSave mentoringSave,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.createMentoring(email, mentoringSave, thumbNailImg)));
  }

  @Operation(summary = "멘토링 수정 api", description = "멘토링 수정 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 수정 성공", content =
      @Content(schema = @Schema(implementation = MentoringDto.class)))
  })
  @PutMapping
  public ResponseEntity<MentoringDto> updateMentoring(
      @RequestPart MentoringSave mentoringSave,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg
  ) {

    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringDto.from(mentoringService.updateMentoring(email, mentoringSave, thumbNailImg)));
  }

  @Operation(summary = "멘토링 삭제 api", description = "멘토링 삭제 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 삭제 성공")
  })
  @DeleteMapping("/{mentoringId}")
  public ResponseEntity<Void> deleteMentoring(
      @PathVariable Long mentoringId
  ) {
    mentoringService.deleteMentoring(mentoringId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "멘토링 등록 api", description = "멘토링 등록 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 등록 성공", content =
      @Content(schema = @Schema(implementation = MentoringInfo.class)))
  })

  @GetMapping("/{mentoringId}")
  public ResponseEntity<MentoringInfo> MentoringInfo(
      @PathVariable Long mentoringId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(mentoringService.mentoringInfo(email, mentoringId));
  }

  @Operation(summary = "멘토링 조회 api", description = "멘토링 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 조회 성공", content =
      @Content(schema = @Schema(implementation = Map.class)))
  })
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

  @GetMapping("/{mentoringId}/schedule")
  public ResponseEntity<List<ScheduleInfo>> scheduleInfoByPeriod(
      @PathVariable Long mentoringId,
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
  ){
    return ResponseEntity.ok(ScheduleInfo.from(scheduleService.scheduleInfoByPeriod(mentoringId, startDate, endDate)));
  }

  @Operation(summary = "멘토링 찜 api", description = "멘토링 찜 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 찜 성공", content =
      @Content(schema = @Schema(implementation = MentoringDto.class)))
  })
  @PostMapping("/{mentoringId}")
  public ResponseEntity<Void> mentoringLike(
      @PathVariable Long mentoringId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    mentoringService.mentoringFollow(email, mentoringId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/follow")
  public ResponseEntity<Page<MentoringList>> getFollowMentoring(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringList.from(mentoringService.getFollowMentoring(email,pageable)));
  }

  @GetMapping("/history")
  public ResponseEntity<Page<MentoringList>> getMentoringHistory(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringList.from(mentoringService.getMentoringHistory(email,pageable)));
  }

  @GetMapping("/history/participated")
  public ResponseEntity<Page<MentoringList>> getParticipatedMentoringHistory(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(MentoringList.from(mentoringService.getParticipatedMentoringHistory(email,pageable)));
  }
}
