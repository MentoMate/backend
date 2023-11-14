package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.PrivateMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PrivateChatMessageInfo {
  private Long privateChatRoomId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;

  public static PrivateChatMessageInfo fromEntity(PrivateMessage privateMessage) {
    return PrivateChatMessageInfo.builder()
        .privateChatRoomId(privateMessage.getPrivateChatRoom().getId())
        .senderNickName(privateMessage.getSenderNickName())
        .message(privateMessage.getMessage())
        .registerDatetime(privateMessage.getRegisterDatetime())
        .build();
  }

}
