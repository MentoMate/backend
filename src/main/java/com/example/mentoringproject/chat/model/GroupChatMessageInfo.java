package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.GroupMessage;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
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
  private Long senderUserId;

  public static GroupChatMessageInfo fromEntity(GroupMessage groupMessage, UserRepository userRepository) {
    String senderNickName = groupMessage.getSenderNickName();
    User user = userRepository.findByNickName(senderNickName);
    Long userId = user.getId();

    return GroupChatMessageInfo.builder()
        .groupMentoringId(groupMessage.getMentoring().getId())
        .senderNickName(groupMessage.getSenderNickName())
        .message(groupMessage.getMessage())
        .registerDatetime(groupMessage.getRegisterDatetime())
        .senderUserId(userId)
        .build();
  }

}
