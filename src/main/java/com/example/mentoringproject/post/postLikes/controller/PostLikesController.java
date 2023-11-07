package com.example.mentoringproject.post.postLikes.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.postLikes.service.PostLikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시판-좋아요", description = "좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/postLikes")
public class PostLikesController {

  private final PostLikesService postLikesService;

  // 좋아요 등록/취소
  @Operation(summary = "좋아요 등록/취소 api", description = "좋아요 등록/취소 api", responses = {
      @ApiResponse(responseCode = "200", description = "좋아요 등록/취소 성공")
  })
  @PostMapping
  public ResponseEntity<String> PostLikes(@PathVariable Long postId) {
    String email = SpringSecurityUtil.getLoginEmail();
    postLikesService.switchPostLikes(email, postId);
    return ResponseEntity.ok("Success");
  }
}
