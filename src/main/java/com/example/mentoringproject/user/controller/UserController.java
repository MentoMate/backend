package com.example.mentoringproject.user.controller;

import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.user.model.UserProfile;
import com.example.mentoringproject.user.model.UserJoinDto;
import com.example.mentoringproject.user.service.UserService;
import java.util.List;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
      @RequestPart UserProfile userProfile,
      @RequestPart(name = "img", required = false) MultipartFile multipartFile

  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.createProfile(email, userProfile, multipartFile)));
  }
  @PutMapping("/profile")
  public ResponseEntity<UserProfile> updateProfile(
      @RequestBody UserProfile userProfile
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfile.from(userService.updateProfile(email, userProfile)));
  }
  @GetMapping("/profile")
  public ResponseEntity<UserProfile>  profileInfo(
      @PathVariable String email
  ) {
    return ResponseEntity.ok(UserProfile.from(userService.profileInfo(email)));
  }


}
