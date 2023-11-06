package com.example.mentoringproject.post.post.controller;

import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.post.model.PostDto;
import com.example.mentoringproject.post.post.model.PostInfoResponseDto;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.post.model.PostUpdateRequest;
import com.example.mentoringproject.post.post.service.PostService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  // 글 등록
  @PostMapping
  public ResponseEntity<PostDto> createPost(
      @RequestPart PostRegisterRequest postRegisterRequest,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg) throws IOException {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(
        PostDto.fromEntity(postService.createPost(email, postRegisterRequest, thumbNailImg)));
  }

  // 글 수정
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,
      @RequestPart PostUpdateRequest postUpdateRequest,
      @RequestPart(name = "thumbNailImg") List<MultipartFile> thumbNailImg) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(PostDto.fromEntity(postService.updatePost(email, postId,
        postUpdateRequest, thumbNailImg)));
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
