package com.example.mentoringproject.chat.model;

import com.example.mentoringproject.chat.entity.GroupChatRoom;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GroupChatRoomCreateResponse {
  private Long id;
  private Mentoring mentoring;
  private LocalDateTime registerDatetime;

  public static GroupChatRoomCreateResponse fromEntity(GroupChatRoom groupChatRoom) {
    return GroupChatRoomCreateResponse.builder()
        .id(groupChatRoom.getId())
        .mentoring(groupChatRoom.getMentoring())
        .registerDatetime(groupChatRoom.getRegisterDatetime())
        .build();
  }

}
