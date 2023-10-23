package com.example.mentoringproject.user.controller;

import com.example.mentoringproject.common.response.ResponseDto;
import com.example.mentoringproject.user.model.UserJoinDto;
import com.example.mentoringproject.user.service.UserService;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;


  @GetMapping("/test")
  public String authTest() {
    return "test-success";
  }

  @PostMapping("/join/email/auth")
  public ResponseEntity<?> sendEmailAuth(@RequestParam("email") String email) {
    userService.sendEmailAuth(email);
    return ResponseEntity.ok("email send success");
  }

  @GetMapping("/join/email/auth")
  public ResponseEntity<?> verifyEmailAuth(@PathParam("auth") String auth) {
    userService.verifyEmailAuth(auth);
    return ResponseEntity.ok("email auth verify success");
  }


  @PostMapping("/join/email")
  public ResponseEntity<?> joinEmailUser(@RequestBody UserJoinDto parameter) {
    userService.joinEmailUser(parameter);
    return ResponseEntity.ok("email join success");
  }
}
