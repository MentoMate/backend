package com.example.mentoringproject.common.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.login.response.ResponseBody;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.model.ReissueToken;
import com.example.mentoringproject.user.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

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
  private static final String EMAIL_CLAIM = "email";

  private static final String BEARER = "Bearer";

  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;

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
    response.setContentType("text/html; charset=utf-8");
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

  public void sendUserIdAndNicknameAndEmail(HttpServletResponse response, Long userId, String nickname, String email)
      throws IOException {
    ResponseBody responseBody= ResponseBody.builder()
        .userId(userId)
        .nickname(nickname)
        .email(email)
        .build();
    response.getWriter().write(objectMapper.writeValueAsString(responseBody));
  }

  public boolean checkTokenValidMoreThanThirtySecondsLeft(String token) {
    try {
      DecodedJWT verify = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
      Date expiresAt = verify.getExpiresAt();
      Date timeLimit = Timestamp.valueOf(LocalDateTime.now().plusSeconds(30));
      if (expiresAt.before(timeLimit)) {
        return false;
      }
      return true;
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }

  public Boolean checkTokenValid(String accessTokenCheck) {
    String accessToken = getAccessToken(accessTokenCheck);
    if (!checkTokenValidMoreThanThirtySecondsLeft(accessToken)) {
      return false;
    }
    return true;
  }

  private static String getAccessToken(String authorization) {
    if (!authorization.startsWith(BEARER)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "잘못된 access 토큰값입니다.");
    }
    return authorization.split(" ")[1];
  }



  public ReissueToken reissueAccessTokenAndRefreshToken(String refreshTokenCheck, String email) {
    User user = userRepository.findByEmail(email).orElseThrow(
            () -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User By Email"));
    String refreshToken = getRefreshToken(refreshTokenCheck);
    checkRefreshTokenAccord(user, refreshToken);
    String reissueRefreshToken = "";
    if (!checkRefreshTokenValidTime(refreshToken)) {
      reissueRefreshToken = reissueRefreshToken(user);
    }
    String accessToken = createAccessToken(email);

    return ReissueToken.builder()
            .accessToken(accessToken)
            .refreshToken(reissueRefreshToken)
            .build();
  }

  private String reissueRefreshToken(User user) {
    String reissueRefreshToken = createRefreshToken();
    user.setRefreshToken(reissueRefreshToken);
    userRepository.save(user);
    return reissueRefreshToken;
  }

  private static String getRefreshToken(String authorizationRefresh) {
    if (!authorizationRefresh.startsWith(BEARER)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "잘못된 refresh 토큰값입니다.");
    }
    return authorizationRefresh.split(" ")[1];
  }

  private static void checkRefreshTokenAccord(User user, String refreshToken) {
    if (!user.getRefreshToken().equals(refreshToken)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "refreshToken값 불일치");
    }
  }

  public boolean checkRefreshTokenValidTime(String refreshToken) {
    try {
      DecodedJWT verify = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken);
      Date expiresAt = verify.getExpiresAt();
      Date timeLimit = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
      if (expiresAt.before(timeLimit)) {
        return false;
      }
      return true;
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }
}
