package com.example.mentoringproject.pay.model;

import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.pay.entity.PayStatus;
import java.time.LocalDate;
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
public class PayDetailDto {
  private String mentoringTitle;
  private String mentorNickname;
  private LocalDate mentoringStartDate;
  private LocalDate mentoringEndDate;
  private LocalDateTime paymentDate;
  private PayStatus payStatus;
  private Integer amount;

  public static PayDetailDto from(Pay pay) {
    return PayDetailDto.builder()
        .mentoringTitle(pay.getMentoring().getTitle())
        .mentorNickname(pay.getMentoring().getUser().getNickName())
        .mentoringStartDate(pay.getMentoring().getStartDate())
        .mentoringEndDate(pay.getMentoring().getEndDate())
        .paymentDate(pay.getUpdateDate())
        .payStatus(pay.getPayStatus())
        .amount(pay.getAmount())
        .build();
  }
}
