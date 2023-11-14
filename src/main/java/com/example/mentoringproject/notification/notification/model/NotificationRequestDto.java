package com.example.mentoringproject.notification.notification.model;

import com.example.mentoringproject.notification.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class NotificationRequestDto {

  @Schema(description = "받는사람 이메일", example = "abc@example.com")
  @Email(message = "receiverEmail은 이메일 형식입니다.")
  @NotBlank(message = "receiverEmail은 필수값입니다.")
  private String receiverEmail;

  @Schema(description = "내용", example = "결제가 완료되었습니다.")
  private String content;

  @Schema(description = "타입", example = "PAY / CHAT")
  private NotificationType notificationType;

}