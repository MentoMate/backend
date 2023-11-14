package com.example.mentoringproject.chat.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupChatMessage {
  private Long groupMentoringId;
  private String senderNickName;
  private String message;
  private LocalDateTime registerDatetime;

}
