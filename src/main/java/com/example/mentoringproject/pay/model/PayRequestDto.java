package com.example.mentoringproject.pay.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayRequestDto {

  @Schema(description = "imp_uid값", example = "imp_uid1234")
  @NotBlank(message = "imp_uid는 필수값입니다.")
  private String imp_uid;

  @Schema(description = "mentoring_id", minimum = "1", example = "1")
  @NotNull(message = "mentoring_id는 필수값입니다.")
  @Min(value = 0, message = "mentoring_id는 0보다 큰 값입니다.")
  private Long mentoring_id;
}
