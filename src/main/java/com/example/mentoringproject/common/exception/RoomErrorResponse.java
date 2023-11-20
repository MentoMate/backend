package com.example.mentoringproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomErrorResponse {
  private String errorMessage;
  private Long privateChatRoomId;

}
