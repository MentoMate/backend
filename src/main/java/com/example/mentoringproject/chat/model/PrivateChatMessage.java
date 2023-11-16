package com.example.mentoringproject.chat.model;


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
public class PrivateChatMessage {
  private Long privateChatRoomId;
  private String message;
  private Long userId;

}
