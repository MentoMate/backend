package com.example.mentoringproject.notification.notification.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.notification.notification.emitter.repository.EmitterRepository;
import com.example.mentoringproject.notification.notification.entity.Notification;
import com.example.mentoringproject.notification.notification.model.NotificationRequestDto;
import com.example.mentoringproject.notification.notification.model.NotificationResponseDto;
import com.example.mentoringproject.notification.notification.repository.NotificationRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import java.io.IOException;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;
  private final UserService userService;

  public SseEmitter subscribe(String userEmail) {
    String emitterId = makeTimeIncludeId(userEmail);
    SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
    emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
    emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

    // 503 에러를 방지하기 위한 더미 이벤트 전송
    String eventId = makeTimeIncludeId(userEmail);
    sendNotification(emitter, eventId, emitterId,
        "EventStream Created. [userId=" + userEmail + "]");

    return emitter;
  }

  private String makeTimeIncludeId(String userEmail) {
    return userEmail + "_" + System.currentTimeMillis();
  }

  public void send(NotificationResponseDto notificationDto) {
    String receiverEmail = notificationDto.getReceiverEmail();
    String eventId = receiverEmail + "_" + System.currentTimeMillis();
    Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserEmail(
        receiverEmail);
    emitters.forEach(
        (key, emitter) -> {
          sendNotification(emitter, eventId, key, notificationDto);
        }
    );
  }

  private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .data(data));
    } catch (IOException exception) {
      //emitter가 타임 아웃 난 경우 에러가 남, emitterRepository에서 제거
      log.debug("emitter timeout error");
      emitterRepository.deleteById(emitterId);
    }
  }

  public NotificationResponseDto saveNotification(NotificationRequestDto parameter) {
    User user = userService.getUser(parameter.getReceiverEmail());

    return NotificationResponseDto.from(notificationRepository.save(Notification.builder()
        .receiverEmail(parameter.getReceiverEmail())
        .receiver(user)
        .content(parameter.getContent())
        .notificationType(parameter.getNotificationType())
        .isRead(false)
        .build()));
  }

  public Page<NotificationResponseDto> getNotification(String email, Pageable pageable) {
    Page<Notification> notificationPage = notificationRepository.findAllByReceiverEmail(email,
        pageable);
    return NotificationResponseDto.from(notificationPage);
  }

  @Transactional
  public NotificationResponseDto readNotification(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Notification Not found"));
    notification.setIsRead(true);
    return NotificationResponseDto.from(notification);
  }

  @Transactional
  public void deleteNotification(Long notificationId) {
    if (!notificationRepository.existsById(notificationId)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지 아이디입니다");
    }
    notificationRepository.deleteById(notificationId);
  }
}
