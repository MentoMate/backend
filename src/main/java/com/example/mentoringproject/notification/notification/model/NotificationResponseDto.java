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
public class NotificationResponseDto {


  private Long notificationId;
  private String receiverEmail;
  private String content;
  private Boolean isRead;
  private NotificationType notificationType;
  private String registerDate;

  public static NotificationResponseDto from(Notification notification) {
    return NotificationResponseDto.builder()
        .notificationId(notification.getId())
        .receiverEmail(notification.getReceiverEmail())
        .content(notification.getContent())
        .isRead(notification.getIsRead())
        .notificationType(notification.getNotificationType())
        .registerDate(String.valueOf(notification.getRegisterDate()))
        .build();
  }

  public static Page<NotificationResponseDto> from(Page<Notification> notificationPage) {
    return notificationPage.map(
        notification -> NotificationResponseDto.builder()
            .notificationId(notification.getId())
            .receiverEmail(notification.getReceiverEmail())
            .content(notification.getContent())
            .isRead(notification.getIsRead())
            .registerDate(String.valueOf(notification.getRegisterDate()))
            .build());
  }
}
