package com.example.mentoringproject.user.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.login.email.components.MailComponents;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.model.UserJoinDto;
import com.example.mentoringproject.user.model.UserProfile;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;
  private final MailComponents mailComponents;

  //인증 확인 이메일을 보내고 DB에 저장
  @Transactional
  public void sendEmailAuth(String email) {
    verifyExistEmail(email);

    String authCode = String.valueOf((int) (Math.random() * 899999) + 100000);
    sendEmailAuth(email, authCode);
    userRepository.save(
        User.builder()
            .email(email)
            .emailAuth(authCode)
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
        .orElseThrow(() -> new RuntimeException("이미 존재하는 이메일입니다."));

    //회원가입이 완료된 이메일이면 exception
    if (user.getRegisterDate()!=null) {
      throw new RuntimeException("이미 가입된 이메일입니다.");
    }

    //회원가입이 완료되지 않은 이메일이면 삭제해야됨
    userRepository.deleteByEmail(email);

  }

  //인증을 위한 이메일 보내기
  private void sendEmailAuth(String email, String authCode) {
    String subject = "MentoringProject에 가입을 축하드립니다.";
    String text = "<p>MentoringProject에 가입을 축하드립니다.</p><p>인증 코드 : " + authCode + "</p>";
    mailComponents.sendMail(email, subject, text);
  }


  //이메일 인증 확인
  @Transactional
  public void verifyEmailAuth(String email, String authCode) {
    User user = userRepository.findByEmailAndEmailAuth(email, authCode).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found email auth")
    );
    if (!user.getEmailAuth().equals(authCode)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Wrong AuthCode");
    }
    user.setEmailAuthDate(LocalDateTime.now());
  }

  public void checkDuplicateNickName(String nickName) {
    Optional<User> user = userRepository.findByNickNameAndRegisterDateIsNotNull(nickName);
    if (user.isPresent()) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
    }
  }


  @Transactional
  public void joinEmailUser(UserJoinDto parameter) {
    User user = userRepository.findByEmail(parameter.getEmail())
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다."));

    if (user.getEmailAuth().isEmpty()) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다.");
    }

    checkDuplicateNickName(parameter.getNickName());

    user.setNickName(parameter.getNickName());
    user.setPassword(encoder.encode(parameter.getPassword()));
    user.setRegisterDate(LocalDateTime.now());

  }

  @Transactional
  public void createProfile(String email, UserProfile userProfile) {
    User user = getUser(email);

    if(userRepository.existsByIdAndNameIsNotNull(user.getId())){
      throw new RuntimeException("프로필이 등록 되어 있습니다.");
    }

    setProfile(user, userProfile);
    userRepository.save(user);
  }

  @Transactional
  public void updateProfile(String email, UserProfile userProfile) {
    User user = getUser(email);

    if(!userRepository.existsByIdAndNameIsNotNull(user.getId())){
      throw new RuntimeException("프로필이 등록 되어 있지 않습니다.");
    }
    setProfile(user, userProfile);
    userRepository.save(user);
  }

  private User setProfile(User user, UserProfile userProfile){

    user.setName(userProfile.getName());
    user.setCareer(userProfile.getCareer());
    user.setIntroduce(userProfile.getIntroduce());
    user.setMainCategory(userProfile.getMainCategory());
    user.setMiddleCategory(userProfile.getMiddleCategory());
    user.setImgUrl(userProfile.getImgUrl());

    return  user;
  }

  public UserProfile profileInfo(String email){
    return UserProfile.from(getUser(email));
  }

  public User getUser(String email){
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."));
  }
}