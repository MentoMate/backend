package com.example.mentoringproject.notification.notification.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationFinalDto {

  @Schema(description = "NotificationType", example = "SUBSCRIBE/RECEIVE")
  private NotificationConnectionType type;
  @Schema(description = "공지 데이터", example = "Json형식의 데이터")
  private Object data;

}
