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
public class GroupChatMessage {
  private Long groupMentoringId;
  private String message;
  private Long userId;

}
