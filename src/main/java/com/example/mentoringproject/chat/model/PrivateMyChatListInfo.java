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
  private String senderNickName; // 실제 메시지 보낸 사람
  private String message;
  private LocalDateTime registerDatetime;
  private String chatPartner; // 로그인 한 사람 기준으로 대화상대


  public static PrivateMyChatListInfo fromEntity(PrivateMessage privateMessage, String nickName) {

    String chatPartner;
    if (nickName.equals(privateMessage.getPrivateChatRoom().getMentor().getNickName())) {
      chatPartner = privateMessage.getPrivateChatRoom().getUser().getNickName();
    } else {
      chatPartner = privateMessage.getPrivateChatRoom().getMentor().getNickName();
    }


    return PrivateMyChatListInfo.builder()
        .id(privateMessage.getId())
        .privateChatRoomId(privateMessage.getPrivateChatRoom().getId())
        .senderNickName(privateMessage.getSenderNickName())
        .message(privateMessage.getMessage())
        .registerDatetime(privateMessage.getRegisterDatetime())
        .chatPartner(chatPartner)
        .build();
  }

}
