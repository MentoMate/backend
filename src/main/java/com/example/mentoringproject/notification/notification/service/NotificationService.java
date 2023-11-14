package com.example.mentoringproject.notification.notification.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.notification.notification.emitter.repository.EmitterRepository;
import com.example.mentoringproject.notification.notification.entity.Notification;
import com.example.mentoringproject.notification.notification.model.NotificationConnectionType;
import com.example.mentoringproject.notification.notification.model.NotificationDto;
import com.example.mentoringproject.notification.notification.model.NotificationFinalDto;
import com.example.mentoringproject.notification.notification.model.NotificationRequestDto;
import com.example.mentoringproject.notification.notification.model.NotificationResponseDto;
import com.example.mentoringproject.notification.notification.repository.NotificationRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

  private static final Long DEFAULT_TIMEOUT = 1000 * 60 * 60L;

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
    NotificationFinalDto connection = NotificationFinalDto.builder()
        .type(NotificationConnectionType.SUBSCRIBE)
        .data("EventStream Created. [userId=" + userEmail + "]")
        .build();
    sendNotification(emitter, eventId, emitterId, connection);

    return emitter;
  }

  private String makeTimeIncludeId(String userEmail) {
    return userEmail + "_" + System.currentTimeMillis();
  }

  public void send(NotificationResponseDto notificationResponseDto) {
    String receiverEmail = notificationResponseDto.getData().getReceiverEmail();
    log.debug("receiverEmail = {}", receiverEmail);
    String eventId = receiverEmail + "_" + System.currentTimeMillis();
    Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserEmail(
        receiverEmail);
    log.debug("emitter = {}", emitters);
    emitters.forEach(
        (key, emitter) -> {
          sendNotification(emitter, eventId, key, notificationResponseDto);
        }
    );
  }

  private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
    try {
      log.debug("sendNotification emitterId = {}", emitterId);
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

    NotificationDto sendData = NotificationDto.from(notificationRepository.save(Notification.builder()
        .receiverEmail(parameter.getReceiverEmail())
        .receiver(user)
        .content(parameter.getContent())
        .notificationType(parameter.getNotificationType())
        .isRead(false)
        .build()));
    return NotificationResponseDto.builder()
        .type(NotificationConnectionType.RECEIVE)
        .data(sendData)
        .build();
  }

  public Page<NotificationDto> getNotification(String email, Pageable pageable) {
    Page<Notification> notificationPage = notificationRepository.findAllByReceiverEmail(email,
        pageable);
    return NotificationDto.from(notificationPage);
  }

  @Transactional
  public NotificationDto readNotification(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Notification Not found"));
    notification.setIsRead(true);
    return NotificationDto.from(notification);
  }

  @Transactional
  public void deleteNotification(Long notificationId) {
    if (!notificationRepository.existsById(notificationId)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "존재하지 않는 공지 아이디입니다");
    }
    notificationRepository.deleteById(notificationId);
  }

  public List<NotificationDto> getUnreadNotification(String email) {
    User user = userService.getUser(email);
    return notificationRepository.findAllByReceiverAndIsReadIsFalseOrderByRegisterDateDesc(user)
        .stream().map(NotificationDto::from)
        .collect(Collectors.toList());
  }
}
