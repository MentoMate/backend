package com.example.mentoringproject.pay.model;

import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.pay.entity.PayStatus;
import java.time.LocalDateTime;
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
public class PayDto{

  private Long id;
  private Long mentoringId;
  private Long userId;
  private String impUid;
  private Integer amount;
  private PayStatus payStatus;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  public static PayDto from(Pay pay) {
    return PayDto.builder()
        .id(pay.getId())
        .mentoringId(pay.getMentoring().getId())
        .userId(pay.getUser().getId())
        .impUid(pay.getImpUid())
        .amount(pay.getAmount())
        .payStatus(pay.getPayStatus())
        .registerDate(pay.getRegisterDate())
        .updateDate(pay.getUpdateDate())
        .build();
  }
}
