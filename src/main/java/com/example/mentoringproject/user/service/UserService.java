package com.example.mentoringproject.user.service;

import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.login.email.components.MailComponents;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.model.UserJoinDto;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;
  private final MailComponents mailComponents;
  private final JwtService jwtService;

  private static final String EMAIL_VERIFY_URL = "http://localhost:8080/user/join/email/auth";

  //인증 확인 이메일을 보내고 DB에 저장
  @Transactional
  public void sendEmailAuth(String email) {
    verifyExistEmail(email);

    String uuid = UUID.randomUUID().toString();
    sendEmailAuth(email, uuid);
    userRepository.save(
        User.builder()
            .email(email)
            .emailAuth(uuid)
            .build()
    );
  }

  private void verifyExistEmail(String email) {

    //이메일이 존재하지 않으면 이메일 인증 가능
    if (!userRepository.existsByEmail(email)) {
      return;
    }

    //만약 이미 이메일이 존재하면 회원가입이 완료된 이메일인지 확인
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("find email server error"));

    //회원가입이 완료된 이메일이면 exception
    if (user.getRegisterDate()!=null) {
      throw new RuntimeException("이미 가입된 이메일입니다.");
    }

    //회원가입이 완료되지 않은 이메일이면 삭제해야됨
    userRepository.deleteByEmail(email);

  }

  //인증을 위한 이메일 보내기
  private void sendEmailAuth(String email, String uuid) {
    String subject = "MentoringProject에 가입을 축하드립니다.";
    String text = "<p>MentoringProject에 가입을 축하드립니다.</p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>" +
        "<div><a href='" + EMAIL_VERIFY_URL + "?auth=" + uuid + "'>가입완료</a></div>";
    mailComponents.sendMail(email, subject, text);
  }


  //이메일 인증 확인
  @Transactional
  public void verifyEmailAuth(String auth) {
    User user = userRepository.findByEmailAuth(auth).orElseThrow(
        () -> new RuntimeException("Not Found email auth")
    );
    user.setEmailAuthDate(LocalDateTime.now());
  }


  @Transactional
  public void joinEmailUser(UserJoinDto parameter) {
    User user = userRepository.findByEmail(parameter.getEmail())
        .orElseThrow(() -> new RuntimeException("이메일 인증이 필요합니다."));

    if (user.getEmailAuth().isEmpty()) {
      throw new RuntimeException("이메일 인증이 필요합니다.");
    }

    user.setNickName(parameter.getNickName());
    user.setPassword(encoder.encode(parameter.getPassword()));
    user.setRegisterDate(LocalDateTime.now());

  }

}