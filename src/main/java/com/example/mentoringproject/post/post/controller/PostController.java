package com.example.mentoringproject.post.post.controller;

import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.post.model.PostDto;
import com.example.mentoringproject.post.post.model.PostInfoResponseDto;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.post.model.PostUpdateRequest;
import com.example.mentoringproject.post.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "게시판", description = "Post API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  // 글 등록
  @Operation(summary = "Post 등록 api", description = "Post 등록 api", responses = {
      @ApiResponse(responseCode = "200", description = "Post 등록 성공", content =
      @Content(schema = @Schema(implementation = PostDto.class)))
  })
  @PostMapping
  public ResponseEntity<PostDto> createPost(@RequestBody PostRegisterRequest postRegisterRequest)
      throws IOException {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(
        PostDto.fromEntity(postService.createPost(email, postRegisterRequest)));
  }

  // 글 수정
  @Operation(summary = "Post 수정 api", description = "Post 수정 api", responses = {
      @ApiResponse(responseCode = "200", description = "Post 수정 성공", content =
      @Content(schema = @Schema(implementation = PostDto.class)))
  })
  @GetMapping("/{postId}")
  public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,
      @RequestBody PostUpdateRequest postUpdateRequest) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(PostDto.fromEntity(postService.updatePost(email, postId,
        postUpdateRequest)));
  }

  // 글 삭제
  @Operation(summary = "Post 삭제 api", description = "Post 삭제 api", responses = {
      @ApiResponse(responseCode = "200", description = "Post 삭제 성공")
  })
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
    String email = SpringSecurityUtil.getLoginEmail();
    postService.deletePost(email, postId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Post 조회 api", description = "Post 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "Post 조회 성공", content =
      @Content(schema = @Schema(implementation = PostInfoResponseDto.class)))
  })
  @GetMapping("/{postId}/info")
  public ResponseEntity<PostInfoResponseDto> postInfo(@PathVariable Long postId) {
    return ResponseEntity.ok(PostInfoResponseDto.fromEntity(postService.postInfo(postId)));
  }

}
