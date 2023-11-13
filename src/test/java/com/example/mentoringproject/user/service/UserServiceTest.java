package com.example.mentoringproject.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.example.mentoringproject.ElasticSearch.mentor.repository.MentorSearchRepository;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.login.email.components.MailComponents;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.model.UserJoinDto;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;

import com.example.mentoringproject.user.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private BCryptPasswordEncoder encoder;
  @Mock
  private MailComponents mailComponents;
  @Mock
  private MentorSearchRepository mentorSearchRepository;
  @Mock
  private S3Service s3Service;

  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("이메일 중복확인 성공 - db에 이메일이 존재하지 않는경우 true 반환")
  void verifyExistEmailSuccess() {
    //given
    given(userRepository.existsByEmail(anyString()))
        .willReturn(false);
    //when
    boolean check = userService.verifyExistEmail(anyString());
    //then
    assertTrue(check);
  }

  @Test
  @DisplayName("이메일 중복확인 - db에 가입 미완료 이메일인경우 db에서 삭제")
  void verifyExistEmailSuccessEmailExist() {
    //given
    given(userRepository.existsByEmail(anyString()))
        .willReturn(true);
    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(User.builder()
            .id(1L)
            .registerDate(null)
            .build()));
    //when
    boolean check = userService.verifyExistEmail(anyString());

    //then
    assertTrue(check);
    verify(userRepository, atMostOnce()).deleteByEmail(anyString());
  }

  @Test
  @DisplayName("이메일 중복확인 실패 -이미 가입된 이메일일경우")
  void verifyExistEmailFail() {
    //given
    given(userRepository.existsByEmail(anyString()))
        .willReturn(true);
    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(User.builder()
            .id(1L)
            .registerDate(LocalDateTime.now())
            .build()));
    //when
    AppException exception = assertThrows(AppException.class,
        () -> userService.verifyExistEmail("email"));

    //then
    assertEquals(exception.getErrorCode(), BAD_REQUEST);
    assertEquals(exception.getMessage(), "이미 가입된 이메일입니다.");

  }

  @Test
  @DisplayName("이메일 전송 성공")
  void sendEmailAuthSuccess() {
    //given
    String email = "test@example.com";

    //when
    userService.sendEmailAuth(email);

    //then
    verify(userRepository, times(1)).save(any(User.class));

  }

  @Test
  @DisplayName("이메일 인증 실패 - 이메일과 인증코드번호 불일치")
  void verifyEmailAuthFail() {
    //given
    given(userRepository.findByEmailAndEmailAuth(anyString(), anyString()))
        .willReturn(Optional.empty());

    //when
    AppException exception = assertThrows(AppException.class, () -> {
      userService.verifyEmailAuth(anyString(), anyString());
    });

    //then
    assertEquals(exception.getErrorCode(), BAD_REQUEST);
  }

  @Test
  @DisplayName("이메일 인증 성공 - emailAuthDate이 null 값이 아닌지 확인")
  void verifyEmailAuthSuccess() {
    //given
    String email = "test@example.com";
    String authCode = "123456";
    User user = User.builder()
        .email(email)
        .emailAuth(authCode)
        .build();
    given(userRepository.findByEmailAndEmailAuth(email, authCode))
        .willReturn(Optional.of(user));
    //when
    userService.verifyEmailAuth(email, authCode);

    //then
    assertNotNull(user.getEmailAuthDate());
  }

  @Test
  @DisplayName("닉네임 중복체크 성공 - 중복 없는경우")
  void checkDuplicateNickNameSuccess() {
    //given
    given(userRepository.findByNickNameAndRegisterDateIsNotNull(anyString()))
        .willReturn(Optional.empty());

    //when
    Boolean checkNickname = userService.checkDuplicateNickName("nickname");

    //then
    assertEquals(checkNickname, true);
  }

  @Test
  @DisplayName("닉네임 중복체크 실패 - 중복이 있는 경우")
  void checkDuplicateNickNameFail() {
    //given
    given(userRepository.findByNickNameAndRegisterDateIsNotNull(anyString()))
        .willReturn(Optional.of(User.builder()
            .nickName("nickname")
            .build()));

    //when
    AppException exception = assertThrows(AppException.class,
        () -> userService.checkDuplicateNickName("nickname"));

    //then
    assertEquals(BAD_REQUEST, exception.getErrorCode());
  }

  @Test
  @DisplayName("회원가입 실패 - db에 이메일이 없는경우 즉, 이메일 인증을 안한 경우")
  void joinEmailUserFail_NotEmailExistInDB() {
    //given
    UserJoinDto userJoinDto = new UserJoinDto(
        "test@email.com", "1234", "nickname");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.empty());

    //when
    AppException exception = assertThrows(AppException.class, () -> {
      userService.joinEmailUser(userJoinDto);
    });

    //then
    assertEquals(exception.getErrorCode(), BAD_REQUEST);
  }

  @Test
  @DisplayName("회원가입 실패 - 이메일 인증을 안한 경우(emailAuthDate가 null인경우)")
  void joinEmailUserFail_NotEmailAuth() {
    //given

    String email = "test@email.com";

    User user = User.builder()
        .email(email)
        .emailAuthDate(null)
        .build();

    UserJoinDto userJoinDto = new UserJoinDto(
        email, "1234", "nickname");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(user));
    //when
    AppException exception = assertThrows(AppException.class, () -> {
      userService.joinEmailUser(userJoinDto);
    });

    //then
    assertEquals(exception.getErrorCode(), BAD_REQUEST);
    assertEquals(exception.getMessage(), "이메일 인증이 필요합니다.");
  }


}