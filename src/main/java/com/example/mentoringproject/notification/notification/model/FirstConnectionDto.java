package com.example.mentoringproject.notification.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirstConnectionDto {

  private String connectionId;
  private String emitterId;

}
