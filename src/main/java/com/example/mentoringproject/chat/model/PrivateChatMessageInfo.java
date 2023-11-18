package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.PrivateMessage;
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
public class PrivateChatMessageInfo {
  private Long privateChatRoomId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;
  private Long senderUserId;

  public static PrivateChatMessageInfo fromEntity(PrivateMessage privateMessage, UserRepository userRepository) {

    String senderNickName = privateMessage.getSenderNickName();
    User user = userRepository.findByNickName(senderNickName);
    Long userId = user.getId();

    return PrivateChatMessageInfo.builder()
        .privateChatRoomId(privateMessage.getPrivateChatRoom().getId())
        .senderNickName(privateMessage.getSenderNickName())
        .message(privateMessage.getMessage())
        .registerDatetime(privateMessage.getRegisterDatetime())
        .senderUserId(userId)
        .build();
  }

}
