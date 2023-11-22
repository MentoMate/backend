package com.example.mentoringproject.test;

import com.example.mentoringproject.common.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/500")
  public void error500() throws IllegalAccessException {
    throw new IllegalAccessException();
  }
  @GetMapping("/400")
  public void error400() throws Exception {
    throw new Exception();
  }

  @GetMapping("/app/400")
  public ResponseEntity<?> errorApp400(@RequestParam Boolean check) {

    if (check) {
      throw new AppException(HttpStatus.BAD_REQUEST, "400errorTest");
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("/app/500")
  public void errorApp500() {
    throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "500error test");
  }
}
