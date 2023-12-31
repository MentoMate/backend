package com.example.mentoringproject.notification.notification.controller;

import static com.example.mentoringproject.common.util.SpringSecurityUtil.getLoginEmail;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.notification.notification.model.NotificationObjectDto;
import com.example.mentoringproject.notification.notification.model.NotificationRequestDto;
import com.example.mentoringproject.notification.notification.model.NotificationDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import com.example.mentoringproject.notification.redis.RedisPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림", description = "알림 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final RedisPublisher redisPublisher;


  @Operation(summary = "알림 최초 연결 api", description = "알림 최초 연결 api", responses = {
      @ApiResponse(responseCode = "200", description = "최초 연결은 data:EventStream Created. [userId=sungjinny5@naver.com] 형식"
      , content = @Content(schema = @Schema(implementation = NotificationObjectDto.class)))
  })
  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public ResponseEntity<SseEmitter> subscribe() {
    String email = SpringSecurityUtil.getLoginEmail();
    log.debug("call subscribe, user={}", email);
    String emitterId = notificationService.makeTimeIncludeId(email);
    return ResponseEntity.ok(notificationService.subscribe(email, emitterId));
  }

  @Operation(summary = "알림 푸쉬 api", description = "알림 푸쉬 api", responses = {
      @ApiResponse(responseCode = "200", description = "대상자에게 알림 푸쉬")
  })
  @PostMapping("/publish/notification")
  public void pushMessage(@Valid @RequestBody NotificationRequestDto parameter) {
    log.debug("call publish, notification={}", parameter);
    redisPublisher.publishNotification(parameter);
  }

  @Operation(summary = "emitterId 삭제", description = "emitterId 삭제", responses = {
      @ApiResponse(responseCode = "200", description = "true/false")
  })
  @DeleteMapping("/emitter")
  public ResponseEntity<Boolean> deleteEmitter(@RequestParam String emitterId) {
    log.debug("emitterId = {}", emitterId);
    notificationService.deleteEmitter(emitterId);
    return ResponseEntity.ok(true);
  }


  @Operation(summary = "알림 목록 조회 api", description = "나에게온 알림 최신순으로 10개씩 목록 조회", responses = {
      @ApiResponse(responseCode = "200", description = "대상자에게 알림 푸쉬", content =
      @Content(schema = @Schema(implementation = NotificationDto.class)))
  })
  @GetMapping("/notification")
  public ResponseEntity<Page<NotificationDto>> getNotification(
      @PageableDefault(sort = "registerDate", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    String email = getLoginEmail();
    return ResponseEntity.ok(notificationService.getNotification(email, pageable));
  }

  @Operation(summary = "알림 읽기 api", description = "알림 읽기", responses = {
      @ApiResponse(responseCode = "200", description = "알림 읽음", content =
      @Content(schema = @Schema(implementation = NotificationDto.class)))
  })
  @PutMapping("/read/notification")
  public ResponseEntity<NotificationDto> readNotification(@RequestParam("notificationId") @Min(1) Long notificationId) {
    return ResponseEntity.ok(notificationService.readNotification(notificationId));
  }

  @Operation(summary = "알림 삭제 api", description = "알림 삭제", responses = {
      @ApiResponse(responseCode = "200", description = "알림 삭제 ture값 반환")
  })
  @DeleteMapping("/notification")
  public ResponseEntity<Boolean> deleteNotification(
      @RequestParam("notificationId") Long notificationId) {
    notificationService.deleteNotification(notificationId);
    return ResponseEntity.ok(true);
  }

  @Operation(summary = "읽지 않은 알림 목록", description = "읽지 않은 알림 목록 가져오기 api", responses = {
      @ApiResponse(responseCode = "200", description = "알림 목록 가져오기 성공 읽음, 리스트로 가져옴", content =
      @Content(schema = @Schema(implementation = NotificationDto.class)))
  })
  @GetMapping("/notification/unread")
  public ResponseEntity<List<NotificationDto>> getUnreadNotification() {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(notificationService.getUnreadNotification(email));
  }
}
