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
public class PrivateChatRoomCreateResponse {
  private Long userId;
  private Long mentorId;
  private Long mentoringId;
  private LocalDateTime registerDatetime;


}
