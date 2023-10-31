package com.example.mentoringproject.user.controller;

import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.user.model.UserProfile;
import com.example.mentoringproject.user.model.UserJoinDto;
import com.example.mentoringproject.user.service.UserService;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping("/test")
  public String authTest() {
    return "test-success";
  }

  @PostMapping("/join/email/auth")
  public ResponseEntity<String> sendEmailAuth(@RequestParam("email") String email) {
    userService.sendEmailAuth(email);
    return ResponseEntity.ok("email send success");
  }

  @GetMapping("/join/email/auth/verify")
  public ResponseEntity<String> verifyEmailAuth(@PathParam("auth") String auth) {
    userService.verifyEmailAuth(auth);
    return ResponseEntity.ok("email auth verify success");
  }


  @PostMapping("/join/email")
  public ResponseEntity<String> joinEmailUser(@RequestBody UserJoinDto parameter) {
    userService.joinEmailUser(parameter);
    return ResponseEntity.ok("email join success");
  }

  @PostMapping("/profile")
  public ResponseEntity<UserProfile> createProfile(
      @RequestBody UserProfile userProfile
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.createProfile(email, userProfile)));
  }
  @PutMapping("/profile")
  public ResponseEntity<UserProfile> updateProfile(
      @RequestBody UserProfile userProfile
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.updateProfile(email, userProfile)));
  }

  @GetMapping
  public ResponseEntity<UserProfile>  profileInfo() {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.profileInfo(email)));
  }
}
