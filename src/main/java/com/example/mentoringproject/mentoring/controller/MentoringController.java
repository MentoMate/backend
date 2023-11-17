package com.example.mentoringproject.mentoring.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.model.MentoringList;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.user.rating.model.ReviewRequestDto;
import com.example.mentoringproject.user.rating.model.ReviewResponseDto;
import com.example.mentoringproject.user.rating.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private final ReviewService ratingService;
  @Value("${iamport.key}")
  private String restApiKey;
  @Value("${iamport.secret}")
  private String restApiSecret;

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
    return ResponseEntity.ok(
        MentoringDto.from(mentoringService.createMentoring(email, mentoringSave, thumbNailImg)));
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
    return ResponseEntity.ok(
        MentoringDto.from(mentoringService.updateMentoring(email, mentoringSave, thumbNailImg)));
  }

  @Operation(summary = "멘토링 삭제 api", description = "멘토링 삭제 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 삭제 성공")
  })
  @DeleteMapping("/{mentoringId}")
  public ResponseEntity<Void> deleteMentoring(
      @PathVariable Long mentoringId
  ) {
    mentoringService.deleteMentoring(mentoringId, restApiKey, restApiSecret);
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
    mentoringMainPageDtoMap.put("MentoringByCountWatch",
        mentoringService.getMentoringByCountWatch());
    mentoringMainPageDtoMap.put("MentorByRating", mentoringService.getMentorByRating());
    mentoringMainPageDtoMap.put("PostRegisterDateTime",
        mentoringService.getPostByRegisterDateTime());
    mentoringMainPageDtoMap.put("MentoringByEndDate", mentoringService.getMentoringByEndDate());
    mentoringMainPageDtoMap.put("Count", mentoringService.getCount());
    return ResponseEntity.ok(mentoringMainPageDtoMap);
  }

  @Operation(summary = "멘토링 일정 조회 api", description = "멘토링 일정 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 일정 조회 성공", content =
      @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleInfo.class))))
  })
  @GetMapping("/{mentoringId}/schedule")
  public ResponseEntity<List<ScheduleInfo>> scheduleInfoByPeriod(
      @PathVariable Long mentoringId,
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
  ) {
    return ResponseEntity.ok(
        ScheduleInfo.from(scheduleService.scheduleInfoByPeriod(mentoringId, startDate, endDate)));
  }

  @Operation(summary = "멘토링 종료 api", description = "멘토링 종료 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 종료 성공")
  })
  @PutMapping("/{mentoringId}")
  public ResponseEntity<Void> mentoringFinish(
      @PathVariable Long mentoringId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    mentoringService.mentoringFinish(email, mentoringId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "멘토링 찜 api", description = "멘토링 찜 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 찜 성공")
  })
  @PostMapping("/{mentoringId}")
  public ResponseEntity<Void> mentoringLike(
      @PathVariable Long mentoringId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    mentoringService.mentoringFollow(email, mentoringId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "멘토 팔로우 api", description = "멘토링 팔로우 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토링 팔로우 성공", content =
      @Content(schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/follow")
  public ResponseEntity<Page<MentoringList>> getFollowMentoring(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(
        MentoringList.from(mentoringService.getFollowMentoring(email, pageable)));
  }

  @Operation(summary = "멘토가 진행한 멘토링 조회 api", description = "멘토가 진행한 멘토링 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "멘토가 진행한 멘토링 조회 성공", content =
      @Content(schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/{userId}/history")
  public ResponseEntity<Page<MentoringList>> getMentoringHistory(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);
    return ResponseEntity.ok(
        MentoringList.from(mentoringService.getMentoringHistory(userId, pageable)));
  }

  @Operation(summary = "사용자가 참가중인 멘토링 조회 api", description = "사용자가 참가중인 멘토링 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "사용자가 참가중인 멘토링 조회 성공", content =
      @Content(schema = @Schema(implementation = Page.class)))
  })
  @GetMapping("/history")
  public ResponseEntity<Page<MentoringList>> getParticipatedMentoringHistory(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize) {
    Pageable pageable = PageRequest.of(page - 1, pageSize);
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(
        MentoringList.from(mentoringService.getParticipatedMentoring(email, pageable)));
  }

  @Operation(summary = "MentoringStatus가 Finish인 Mentoring가져오기 API",
      description = "종료된 멘토링 가져오기", responses = {
      @ApiResponse(responseCode = "200", description = "종료된 멘토링 가져오기 성공", content =
      @Content(schema = @Schema(implementation = MentoringDto.class)))
  })
  @GetMapping("/end")
  public ResponseEntity<Page<MentoringDto>> getListOfExpiredMentoring(
      @PageableDefault Pageable pageable
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(mentoringService.getEndedMentoringList(email, pageable));
  }

  @Operation(summary = "평점, 후기 남기기 API", description = "평점, 후기 남기기 API", responses = {
      @ApiResponse(responseCode = "200", description = "평점, 후기 남기기 성공", content =
      @Content(schema = @Schema(implementation = ReviewResponseDto.class)))
  })
  @PutMapping("/rating")
  public ResponseEntity<ReviewResponseDto> giveFeedbackAndRating(
      @RequestBody ReviewRequestDto gradeRequestDto) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(ratingService.giveFeedbackAndRating(email, gradeRequestDto));
  }

  @Operation(summary = "멘토에 대한 후기 가져오기 API", description = "멘토에 대한 후기 최신순으로 가져오기",
      responses = {@ApiResponse(responseCode = "200", description = "멘토에 대한 후기 가져오기 성공",
          content = @Content(schema = @Schema(implementation = ReviewResponseDto.class)))
      })
  @GetMapping("/mentor/review")
  public ResponseEntity<Page<ReviewResponseDto>> getMentorReview(
      @RequestParam Long mentorId,
      @PageableDefault(size = 4, sort = "updateDate", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(ratingService.getMentorReview(mentorId, pageable));
  }
}
