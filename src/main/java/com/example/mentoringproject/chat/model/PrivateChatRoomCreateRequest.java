package com.example.mentoringproject.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PrivateChatRoomCreateRequest {
  private Long mentorId;
  private Long mentoringId;

}
