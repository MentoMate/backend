package com.example.mentoringproject.pay.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.mentee.service.MenteeService;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.pay.entity.PayStatus;
import com.example.mentoringproject.pay.repository.PayRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {

  @Mock
  private PayRepository payRepository;

  @Mock
  private MentoringService mentoringService;

  @Mock
  private UserService userService;

  @Mock
  private MenteeService menteeService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private PayService payService;

  public static final String IAMPORT_ACCESS_TOKEN_URL = "https://api.iamport.kr/users/getToken";
  public static final String IAMPORT_CANCEL_URL = "https://api.iamport.kr/payments/cancel";

  @Test
  @DisplayName("결제 실패 - 이미 신청자 결제완료된 유저")
  void payCompleteRegisterFail_alreadyPayComplete() {
    //given
    Mentoring mentoring = new Mentoring();
    Long mentoringId = 1L;
    mentoring.setId(mentoringId);

    String email = "buyer@example.com";
    User buyer = new User();
    buyer.setId(1L);

    //결제 완료자 리스트에 이미 User가 있는경우
    List<User> menteeList = new ArrayList<>();
    User mentee1 = new User();
    mentee1.setId(1L);
    User mentee2 = new User();
    mentee2.setId(2L);
    menteeList.add(mentee1);
    menteeList.add(mentee2);



    given(mentoringService.getMentoring(anyLong())).willReturn(mentoring);
    given(userService.getUser(anyString())).willReturn(buyer);
    given(menteeService.getMenteeListFormMentoring(any())).willReturn(menteeList);

    //when
    AppException appException = assertThrows(AppException.class, () ->
        payService.payCompleteRegister(email, anyString(), mentoringId));

    //then
    assertEquals(appException.getErrorCode(), HttpStatus.BAD_REQUEST);
    assertEquals(appException.getMessage(), "이미 결제가 완료되었습니다.");
  }


  @Test
  @DisplayName("결제 성공 - payRepository에 save, menteeList에 add 확인")
  void payCompleteRegisterSuccess() {
    //given
    Mentoring mentoring = new Mentoring();
    Long mentoringId = 1L;
    List<User> menteeList = new ArrayList<>();

    mentoring.setId(mentoringId);

    String email = "buyer@example.com";
    User buyer = new User();
    buyer.setId(1L);

    String impUid = "impUid";

    given(mentoringService.getMentoring(anyLong())).willReturn(mentoring);
    given(userService.getUser(anyString())).willReturn(buyer);
    given(menteeService.getMenteeListFormMentoring(any())).willReturn(menteeList);

    //when
    payService.payCompleteRegister(email, impUid, mentoringId);

    //then
    verify(payRepository, times(1)).save(any(Pay.class));
    assertEquals(menteeList.size(), 1);
  }

  @Test
  @DisplayName("결제 취소 실패 - 존재하지 않는 결제정보")
  void payCancelFail_paymentInformationDoesNotExist() {
    //given
    given(payRepository.findById(anyLong()))
        .willReturn(Optional.empty());
    //when
    AppException exception = assertThrows(AppException.class, () ->
        payService.payCancel("example@email.com", 1L, "restApiKey", "restApiSecret"));

    //then
    assertEquals(exception.getErrorCode(), HttpStatus.BAD_REQUEST);
    assertEquals(exception.getMessage(), "존재하지 않는 결제 정보입니다.");
  }

  @Test
  @DisplayName("결제 취소 실패 - 유저 불일치")
  void payCancelFail_userDiscrepancy() {
    //given
    User user = User.builder()
        .id(1L)
        .email("user@example.com")
        .build();

    Pay pay = Pay.builder()
        .user(user)
        .build();

    given(payRepository.findById(anyLong()))
        .willReturn(Optional.of(pay));
    //when
    AppException exception = assertThrows(AppException.class, () ->
        payService.payCancel("unmatch@example.com", 1L, "restApiKey", "restApiSecret"));

    //then
    assertEquals(exception.getErrorCode(), HttpStatus.BAD_REQUEST);
    assertEquals(exception.getMessage(), "결제한 사용자와 요청한 사용자의 정보가 다릅니다.");
  }

  @Test
  @DisplayName("결제 취소 실패 - 이미 취소된 결제")
  void payCancelFail_alreadyCancel() {
    //given
    User user = User.builder()
        .id(1L)
        .email("user@example.com")
        .build();

    Pay pay = Pay.builder()
        .id(1L)
        .user(user)
        .payStatus(PayStatus.CANCEL)
        .build();

    given(payRepository.findById(anyLong()))
        .willReturn(Optional.of(pay));
    //when
    AppException exception = assertThrows(AppException.class, () ->
        payService.payCancel("user@example.com", 1L, "restApiKey", "restApiSecret"));

    //then
    assertEquals(exception.getErrorCode(), HttpStatus.BAD_REQUEST);
    assertEquals(exception.getMessage(), "이미 취소된 결제입니다.");
  }

}
