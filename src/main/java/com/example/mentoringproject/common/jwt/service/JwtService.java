package com.example.mentoringproject.common.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String USER_ID = "UserId";
  private static final String EMAIL_CLAIM = "email";

  private static final String BEARER = "Bearer";

  private final UserRepository userRepository;

  public String createAccessToken(String email) {
    Date now = new Date();
    return JWT.create()
        .withSubject(ACCESS_TOKEN_SUBJECT)
        .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) //토큰 만료시간 설정
        .withClaim(EMAIL_CLAIM, email)
        .sign(Algorithm.HMAC512(secretKey));
  }

  public String createRefreshToken() {
    Date now = new Date();
    return JWT.create()
        .withSubject(REFRESH_TOKEN_SUBJECT)
        .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
        .sign(Algorithm.HMAC512(secretKey));
  }

  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken,
      String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
    log.info("AccessToken, RefreshToken 헤더 설정 완료");
  }

  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader))
        .filter(refreshToken -> refreshToken.startsWith(BEARER))
        .map(refreshToken -> refreshToken.split(" ")[1]);
  }

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader))
        .filter(accessToken -> accessToken.startsWith(BEARER))
        .map(accessToken -> accessToken.split(" ")[1]);
  }

  public Optional<String> extractEmail(String accessToken) {
    try {
      return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
          .build()
          .verify(accessToken)
          .getClaim(EMAIL_CLAIM)
          .asString());
    } catch (Exception e) {
      log.error("액세스 토큰이 유효하지 않습니다. {}", e.getMessage());
      return Optional.empty();
    }
  }

  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(accessHeader, accessToken);
  }

  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(refreshHeader, refreshToken);
  }

  @Transactional
  public void updateRefreshToken(String email, String refreshToken) {
    userRepository.findByEmail(email)
        .ifPresentOrElse(
            user -> user.updateRefreshToken(refreshToken),
            () -> new RuntimeException("일치하는 회원이 없습니다.")
        );
  }

  public boolean isTokenValid(String token) {
    try {
      JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
      return true;
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }

  public void sendUserIdInHeader(HttpServletResponse response, Long userId) {
    response.setHeader(USER_ID, String.valueOf(userId));
  }
}
