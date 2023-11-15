package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.GroupMessage;
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
public class GroupChatMessageResponse {
  private Long groupMentoringId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;

  public static GroupChatMessageResponse fromEntity(GroupMessage groupMessage) {
    return GroupChatMessageResponse.builder()
        .groupMentoringId(groupMessage.getMentoring().getId())
        .senderNickName(groupMessage.getSenderNickName())
        .message(groupMessage.getMessage())
        .registerDatetime(groupMessage.getRegisterDatetime())
        .build();
  }

}
