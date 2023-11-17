package com.example.mentoringproject.login.email.handler;

import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.login.email.user.EmailLoginUser;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication){
    String email = extractUsername(authentication);
    Long userId = extractUserId(authentication);
    String accessToken = jwtService.createAccessToken(email);
    String refreshToken = jwtService.createRefreshToken();

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.sendUserIdInHeader(response, userId);
    userRepository.findByEmail(email)
        .ifPresent(user -> {
          user.updateRefreshToken(refreshToken);
          user.setLastLogin(LocalDateTime.now());
          userRepository.saveAndFlush(user);
        });
    log.debug("로그인에 성공하였습니다. 이메일 : {}", email);
    log.debug("로그인에 성공하였습니다. AccessToken : {}", accessToken);
    log.debug("User Id : {}", userId);
  }

  private String extractUsername(Authentication authentication) {
    EmailLoginUser userDetails = (EmailLoginUser) authentication.getPrincipal();
    return userDetails.getUsername();
  }

  private Long extractUserId(Authentication authentication) {
    EmailLoginUser user = (EmailLoginUser) authentication.getPrincipal();
    return user.getUserId();
  }

}