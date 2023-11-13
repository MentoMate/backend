package com.example.mentoringproject.pay.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.mentee.service.MenteeService;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.pay.entity.PayStatus;
import com.example.mentoringproject.pay.model.IamportResponseDto;
import com.example.mentoringproject.pay.repository.PayRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

  private final PayRepository payRepository;
  private final MentoringService mentoringService;
  private final MenteeService menteeService;
  private final UserService userService;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public static final String IAMPORT_ACCESS_TOKEN_URL = "https://api.iamport.kr/users/getToken";
  public static final String IAMPORT_CANCEL_URL = "https://api.iamport.kr/payments/cancel";

  @Transactional
  public void payCompleteRegister(String email, String impUid, Long mentoringId) {

    Mentoring mentoring = mentoringService.getMentoring(mentoringId);
    User buyer = userService.getUser(email);

    //이미 결제가 완료된 사람인지 확인
    List<User> menteeList = menteeService.getMenteeListFormMentoring(mentoring);

    if (menteeList.stream().anyMatch(user -> user.getId().equals(buyer.getId()))) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이미 결제가 완료되었습니다.");
    }

    //결제 완료, repository에 저장
    Pay pay = payRepository.save(Pay.builder()
        .user(buyer)
        .mentoring(mentoring)
        .impUid(impUid)
        .amount(mentoring.getAmount())
        .payStatus(PayStatus.COMPLETE)
        .build());

    log.debug("Pay Info = {}", pay);
    //mentoring userList에 user추가
    menteeList.add(buyer);
  }

  @Transactional
  public Pay payCancel(String email, Long payId, String restApiKey, String restApiSecret) {
    Pay pay = getPay(payId);

    checkPayUserEmailAndCancelUserEmail(email, pay);

    checkPaymentAlreadyCanceled(pay);

    String accessToken = getAccessToken(restApiKey, restApiSecret);
    log.debug(accessToken);

    cancelPayment(accessToken, pay);

    pay.setPayStatus(PayStatus.CANCEL);
    mentoringService.deleteMentoringUserByCancelPayment(pay);

    return pay;
  }

  private Pay getPay(Long payId) {
    return payRepository.findById(payId).orElseThrow(() ->
        new AppException(HttpStatus.BAD_REQUEST, "존재하지 않는 결제 정보입니다."));
  }

  private static void checkPayUserEmailAndCancelUserEmail(String email, Pay pay) {
    if (!pay.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "결제한 사용자와 요청한 사용자의 정보가 다릅니다.");
    }
  }
  private static void checkPaymentAlreadyCanceled(Pay pay) {
    if (pay.getPayStatus().equals(PayStatus.CANCEL)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이미 취소된 결제입니다.");
    }
  }

  public String getAccessToken(String restApiKey, String restApiSecret) {
    try {
      return getAccessTokenApi(restApiKey, restApiSecret);
    } catch (Exception e) {
      log.debug(e.getMessage());
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "iamport accessToken 가져오기 에러");
    }
  }

  private void cancelPayment(String accessToken, Pay pay) {
    try {
      //서버에서 취소후 결과 코드 받아오기
      String code = cancelPaymentApi(accessToken, pay.getImpUid());
      log.debug("response code = {}", code);
      if (code.equals("1")) {
        throw new AppException(HttpStatus.BAD_REQUEST, "이미 취소된 결제입니다.");
      }
      else if (code.equals("-1")) {
        throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "iamport 서버에러");
      }
    } catch (JsonProcessingException e) {
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  private String getAccessTokenApi(String restApiKey, String restApiSecret)
      throws JsonProcessingException {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    JSONObject jsonBody = new JSONObject();
    jsonBody.put("imp_key", restApiKey);
    jsonBody.put("imp_secret", restApiSecret);

    HttpEntity<String> entity = new HttpEntity<>(jsonBody.toString(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity(IAMPORT_ACCESS_TOKEN_URL, entity, String.class);

    IamportResponseDto dto = objectMapper.readValue(response.getBody(), IamportResponseDto.class);
    return dto.getResponse().get("access_token").toString();
  }

  public String cancelPaymentApi(String accessToken, String impUid) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", accessToken);

    JSONObject jsonBody = new JSONObject();
    jsonBody.put("imp_uid", impUid);

    HttpEntity<String> entity = new HttpEntity<>(jsonBody.toString(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity(IAMPORT_CANCEL_URL, entity, String.class);

    IamportResponseDto dto = objectMapper.readValue(response.getBody(), IamportResponseDto.class);
    return dto.getCode();
  }
}
