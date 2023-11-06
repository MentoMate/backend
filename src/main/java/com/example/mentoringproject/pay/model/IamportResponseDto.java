package com.example.mentoringproject.pay.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IamportResponseDto {

  private String code;
  private String message;
  private Map<String, Object> response;

}
