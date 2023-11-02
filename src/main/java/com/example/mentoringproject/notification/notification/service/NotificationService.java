package com.example.mentoringproject.notification.notification.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.notification.notification.emitter.repository.EmitterRepository;
import com.example.mentoringproject.notification.notification.entity.Notification;
import com.example.mentoringproject.notification.notification.entity.NotificationDto;
import com.example.mentoringproject.notification.notification.repository.NotificationRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;

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

  public void send(NotificationDto notificationDto) {
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
      emitterRepository.deleteById(emitterId);
    }
  }

  public void saveNotification(NotificationDto notificationDto) {
    notificationRepository.save(Notification.builder()
        .receiverEmail(notificationDto.getReceiverEmail())
        .content(notificationDto.getContent())
        .notificationType(notificationDto.getNotificationType())
        .isRead(false)
        .registerDate(LocalDateTime.now())
        .build());
  }

  public List<NotificationDto> getNotification(String email, Pageable pageable) {
    List<Notification> notificationList = notificationRepository.
        findAllByReceiverEmailAndIsReadOrderByRegisterDateDesc(email, false);
    List<NotificationDto> notificationDtoList = new ArrayList<>();

    for (Notification notification : notificationList) {
      notificationDtoList.add(NotificationDto.from(notification));
    }
    return notificationDtoList;
  }

  @Transactional
  public void readNotification(Long id) {
    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Notification Not found"));
    notification.setIsRead(true);
  }
}
