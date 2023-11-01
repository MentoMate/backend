package com.example.mentoringproject.post.post.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.img.service.S3Service;
import com.example.mentoringproject.post.post.model.PostRegisterDto;
import com.example.mentoringproject.post.post.model.PostUpdateDto;
import com.example.mentoringproject.post.post.service.PostService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  // 글 등록
  @PostMapping
  public ResponseEntity<?> createPost(@RequestPart PostRegisterDto postRegisterDto,
      @RequestPart("imgUrl") List<MultipartFile> multipartFiles) throws IOException {
    String email = SpringSecurityUtil.getLoginEmail();

    List<String> imgPaths = s3Service.upload(multipartFiles);
    postService.createPost(email, postRegisterDto, imgPaths);
    return ResponseEntity.ok("Post created successfully!");
  }

  // 글 수정
  @PutMapping("/{postId}")
  public ResponseEntity<?> updatePost(@PathVariable Long postId,
      @RequestBody PostUpdateDto postUpdateDto) {
    String email = SpringSecurityUtil.getLoginEmail();
    postService.updatePost(email, postId, postUpdateDto);
    return ResponseEntity.ok("Post updated successfully!");
  }

  // 글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<?> deletePost(@PathVariable Long postId) {
    String email = SpringSecurityUtil.getLoginEmail();
    postService.deletePost(email, postId);
    return ResponseEntity.ok("Post deleted successfully!");
  }

  // 전체 목록 조회
  @GetMapping
  public ResponseEntity<?> getAllPosts(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);

    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortBy);
    return ResponseEntity.ok(postService.findAllPosts(pageable));
  }

}
