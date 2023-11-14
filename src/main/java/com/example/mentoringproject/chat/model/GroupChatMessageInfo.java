package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.GroupMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupChatMessageInfo {
  private Long groupMentoringId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;

  public static GroupChatMessageInfo fromEntity(GroupMessage groupMessage) {
    return GroupChatMessageInfo.builder()
        .groupMentoringId(groupMessage.getMentoring().getId())
        .senderNickName(groupMessage.getSenderNickName())
        .message(groupMessage.getMessage())
        .registerDatetime(groupMessage.getRegisterDatetime())
        .build();
  }

}
