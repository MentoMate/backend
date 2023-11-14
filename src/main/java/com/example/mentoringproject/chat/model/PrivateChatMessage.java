package com.example.mentoringproject.chat.model;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PrivateChatMessage {
  private Long privateChatRoomId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;

}
