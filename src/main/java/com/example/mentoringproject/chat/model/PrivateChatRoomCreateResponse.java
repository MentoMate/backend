package com.example.mentoringproject.chat.model;

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
public class PrivateChatRoomCreateResponse {
  private Long userId;
  private Long mentorId;
  private Long mentoringId;
  private LocalDateTime registerDatetime;
  private boolean isCreate;
  private Long privateChatRoomId;


}
