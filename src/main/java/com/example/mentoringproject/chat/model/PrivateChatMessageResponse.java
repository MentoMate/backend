package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.PrivateMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PrivateChatMessageResponse {
  private Long privateChatRoomId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;
  private Long userId;

  public static PrivateChatMessageResponse fromEntity(PrivateMessage privateMessage, Long userId) {
    return PrivateChatMessageResponse.builder()
        .privateChatRoomId(privateMessage.getPrivateChatRoom().getId())
        .senderNickName(privateMessage.getSenderNickName())
        .message(privateMessage.getMessage())
        .registerDatetime(privateMessage.getRegisterDatetime())
        .userId(userId)
        .build();
  }

}
