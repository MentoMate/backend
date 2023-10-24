package com.example.mentoringproject.login.oauth2.handler;

import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.login.email.service.LoginService;
import com.example.mentoringproject.login.oauth2.CustomOAuth2User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final LoginService loginService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    log.info("OAuth2 Login 성공!");
    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
  }

  private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {

    String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
    String refreshToken = jwtService.createRefreshToken();
    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    loginService.setLastLogin(oAuth2User);
  }
}