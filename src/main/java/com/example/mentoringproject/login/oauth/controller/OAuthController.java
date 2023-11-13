package com.example.mentoringproject.login.oauth.controller;

import com.example.mentoringproject.login.oauth.service.OAuth2Service;
import com.example.mentoringproject.user.user.entity.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "OAuth2 로그인", description = "OAuth2 로그인 API")
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/login")
public class OAuthController {

  private final OAuth2Service oauthService;

  @GetMapping("/oauth/test/{socialLoginType}")
  public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath) {
    SocialType socialLoginType = SocialType.valueOf(SocialLoginPath.toUpperCase());
    oauthService.sendRedirectUri(socialLoginType);
  }

  @Operation(summary = "카카오 로그인", description = "카카오 로그인 api", responses = {
      @ApiResponse(responseCode = "200", description = "카카오 회원가입 완료로 이동 및 카카오 로그인 성공")
  })
  @ResponseBody
  @GetMapping("/oauth/callback/kakao")
  public void loginKakaoCallback(
      @RequestParam(name = "code") String code) throws IOException {
    oauthService.oAuthLogin(SocialType.KAKAO, code);
  }


  @Operation(summary = "네이버 로그인", description = "네이버 로그인 api", responses = {
      @ApiResponse(responseCode = "200", description = "네이버 회원가입 완료로 이동 및 네이버 로그인 성공")
  })
  @ResponseBody
  @GetMapping(value = "/oauth/callback/naver")
  public void loginCallback(
      @RequestParam(name = "code") String code) throws IOException {
    oauthService.oAuthLogin(SocialType.NAVER, code);
  }
}
