package com.example.mentoringproject.notification.notification.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationDto {

  private String receiverEmail;
  private NotificationType notificationType;
  private String content;
  private LocalDateTime registerDate;

  public static NotificationDto from(Notification notification) {
    return NotificationDto.builder()
        .receiverEmail(notification.getReceiverEmail())
        .notificationType(notification.getNotificationType())
        .content(notification.getContent())
        .registerDate(notification.getRegisterDate())
        .build();
  }
}
