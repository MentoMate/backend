package com.example.mentoringproject.notification.notification.model;

import com.example.mentoringproject.notification.notification.entity.Notification;
import com.example.mentoringproject.notification.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {


  private Long notificationId;
  private String receiverEmail;
  private String content;
  private Boolean isRead;
  private NotificationType notificationType;
  private String registerDate;

  public static NotificationDto from(Notification notification) {
    return NotificationDto.builder()
        .notificationId(notification.getId())
        .receiverEmail(notification.getReceiverEmail())
        .content(notification.getContent())
        .isRead(notification.getIsRead())
        .notificationType(notification.getNotificationType())
        .registerDate(String.valueOf(notification.getRegisterDate()))
        .build();
  }

  public static Page<NotificationDto> from(Page<Notification> notificationPage) {
    return notificationPage.map(
        notification -> NotificationDto.builder()
            .notificationId(notification.getId())
            .receiverEmail(notification.getReceiverEmail())
            .content(notification.getContent())
            .isRead(notification.getIsRead())
            .registerDate(String.valueOf(notification.getRegisterDate()))
            .build());
  }
}
