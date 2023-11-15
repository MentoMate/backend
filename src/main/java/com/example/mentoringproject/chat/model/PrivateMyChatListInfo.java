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
public class PrivateMyChatListInfo {
  private Long id;
  private Long privateChatRoomId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;


  public static PrivateMyChatListInfo fromEntity(PrivateMessage privateMessage) {
    return PrivateMyChatListInfo.builder()
        .id(privateMessage.getId())
        .privateChatRoomId(privateMessage.getPrivateChatRoom().getId())
        .senderNickName(privateMessage.getSenderNickName())
        .message(privateMessage.getMessage())
        .registerDatetime(privateMessage.getRegisterDatetime())
        .build();
  }

}
