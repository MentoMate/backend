package com.example.mentoringproject.pay.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.notification.notification.model.NotificationRequestDto;
import com.example.mentoringproject.notification.notification.service.NotificationService;
import com.example.mentoringproject.notification.redis.RedisPublisher;
import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.pay.model.PayDto;
import com.example.mentoringproject.pay.service.PayService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "결제", description = "결제 API")
@RestController
public class PayController {

  @Value("${iamport.key}")
  private String restApiKey;
  @Value("${iamport.secret}")
  private String restApiSecret;

  private final IamportClient iamportClient;
  private final PayService payService;
  private final NotificationService notificationService;
  private final RedisPublisher redisPublisher;

  public PayController(PayService payService, NotificationService notificationService, RedisPublisher redisPublisher) {
    this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    this.payService = payService;
    this.notificationService = notificationService;
    this.redisPublisher = redisPublisher;
  }

  @Operation(summary = "결제 성공시 db저장 api", description = "결제 성공시 db저장 api", responses = {
      @ApiResponse(responseCode = "200", description = "iamportClient.paymentByImpUid(ImpUid)로 반환")
  })
  @PostMapping("/pay/complete")
  public ResponseEntity<Boolean> paymentComplete(@RequestParam("imp_uid") String impUid,
      @RequestParam("mentoring_id") Long mentoringId)
      throws IamportResponseException, IOException {
    String email = SpringSecurityUtil.getLoginEmail();
    IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
    if (response.getCode() != 0) {
      return ResponseEntity.badRequest().body(false);
    }
    NotificationRequestDto notificationRequestDto = payService.payCompleteRegister(email, impUid,
        mentoringId);
    redisPublisher.publishNotification(notificationRequestDto);
    return ResponseEntity.ok(true);
  }

  @Operation(summary = "결제 취소", description = "payId로 결제 취소 api", responses = {
      @ApiResponse(responseCode = "200", description = "결제 취소 완료")
  })
  @PostMapping("/pay/cancel")
  public ResponseEntity<PayDto> paymentCancel(@RequestParam Long payId) {
    String email = SpringSecurityUtil.getLoginEmail();
    //취소 api 진행
    Pay pay = payService.payCancel(email, payId, restApiKey,
        restApiSecret);
    return ResponseEntity.ok(PayDto.from(pay));
  }
}