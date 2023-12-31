package com.example.mentoringproject.login.oauth.service;


import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.login.email.service.LoginService;
import com.example.mentoringproject.login.oauth.model.OAuthToken;
import com.example.mentoringproject.login.oauth.oauth.KakaoOauth;
import com.example.mentoringproject.login.oauth.oauth.NaverOauth;
import com.example.mentoringproject.user.user.entity.SocialType;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.mentoringproject.user.user.entity.SocialType.KAKAO;
import static com.example.mentoringproject.user.user.entity.SocialType.NAVER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

  private final NaverOauth naverOauth;
  private final KakaoOauth kakaoOauth;
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final LoginService loginService;
  private final HttpServletResponse response;

  // Redirect 요청 함수
  public void sendRedirectUri(SocialType socialType) {
    if (socialType.equals(NAVER)) {
      naverOauth.sendOauthRedirectURL();
      return;
    } else if (socialType.equals(KAKAO)) {
      kakaoOauth.sendOauthRedirectURL();
      return;
    }
    throw new AppException(HttpStatus.BAD_REQUEST, "알수 없는 소셜로그인 형식입니다.");
  }

  public void oAuthLogin(SocialType socialType, String code) throws IOException {
    String socialId;

    if (socialType.equals(NAVER)) {
      ResponseEntity<String> accessTokenResponse = naverOauth.requestAccessToken(code);
      OAuthToken oAuthToken = naverOauth.getAccessToken(accessTokenResponse);
      ResponseEntity<String> userInfoResponse = naverOauth.requestUserInfo(oAuthToken);
      socialId = naverOauth.getUserInfo(userInfoResponse);
    } else {
      log.debug("accessTokenResponse get start");
      ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);
      log.debug("oAuthToken get start");
      OAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);
      log.debug("userInfoResponse get start");
      ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);
      log.debug("getUserInfo get start");
      socialId = kakaoOauth.getUserInfo(userInfoResponse);
    }

    log.debug("user get start");
    Optional<User> optionalOauthUser = userRepository.findBySocialIdAndSocialType(socialId,
        socialType);

    if (optionalOauthUser.isEmpty()) {
      userRepository.save(User.builder()
          .email(UUID.randomUUID() + "@email.com")
          .socialId(socialId)
          .socialType(socialType)
          .nickName(makeRandomNickname())
          .registerDate(LocalDateTime.now())
          .build());
      log.info("oauth join success");
      return;
    }

    User oauthUser = optionalOauthUser.get();
    String email = oauthUser.getEmail();
    Long userId = oauthUser.getId();
    String nickname = oauthUser.getNickName();

    String accessToken = jwtService.createAccessToken(oauthUser.getEmail());
    String refreshToken = jwtService.createRefreshToken();
    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.sendUserIdAndNicknameAndEmail(response, userId, nickname, email);
    jwtService.updateRefreshToken(email, refreshToken);
    log.info("oauth login success");
  }

  private String makeRandomNickname() {
    String randomNickname = UUID.randomUUID().toString().replaceAll("-", "");
    return randomNickname.substring(0, 8) + "@nickname";
  }
}
