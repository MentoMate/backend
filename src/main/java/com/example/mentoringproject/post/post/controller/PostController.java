package com.example.mentoringproject.post.post.controller;

import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.post.model.PostDto;
import com.example.mentoringproject.post.post.model.PostInfoResponseDto;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.post.model.PostUpdateRequest;
import com.example.mentoringproject.post.post.service.PostService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  // 글 등록
  @PostMapping
  public ResponseEntity<PostDto> createPost(@RequestBody PostRegisterRequest postRegisterRequest)
      throws IOException {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(
        PostDto.fromEntity(postService.createPost(email, postRegisterRequest)));
  }

  // 글 수정
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,
      @RequestBody PostUpdateRequest postUpdateRequest) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(PostDto.fromEntity(postService.updatePost(email, postId,
        postUpdateRequest)));
  }

  // 글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
    String email = SpringSecurityUtil.getLoginEmail();
    postService.deletePost(email, postId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{postId}/info")
  public ResponseEntity<PostInfoResponseDto> postInfo(@PathVariable Long postId) {
    return ResponseEntity.ok(PostInfoResponseDto.fromEntity(postService.postInfo(postId)));
  }

}
