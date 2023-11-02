package com.example.mentoringproject.notification.notification.controller;

import static com.example.mentoringproject.common.util.SpringSecurityUtil.getLoginEmail;

import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public ResponseEntity<SseEmitter> subscribe(@RequestParam String email) {
    return ResponseEntity.ok(notificationService.subscribe(email));
  }

  @GetMapping("/notification")
  public ResponseEntity<List<NotificationDto>> getNotification(
      @PageableDefault(size = 10) Pageable pageable
  ) {
    String email = getLoginEmail();
    return ResponseEntity.ok(notificationService.getNotification(email, pageable));
  }

  @PutMapping("/read/notification")
  public ResponseEntity<?> readNotification(@RequestParam("id") Long id) {
    notificationService.readNotification(id);
    return ResponseEntity.ok().build();
  }
}
