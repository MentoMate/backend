package com.example.mentoringproject.post.comment.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.comment.model.CommentRegisterDto;
import com.example.mentoringproject.post.comment.model.CommentUpdateDto;
import com.example.mentoringproject.post.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  // 댓글 등록
  @PostMapping
  public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRegisterDto commentRegisterDto) {
    String email = SpringSecurityUtil.getLoginEmail();
    commentService.createComment(email, postId, commentRegisterDto);
    return ResponseEntity.ok("comment created successfully!");
  }

  // 댓글 수정
  @PutMapping("/{commentId}")
  public ResponseEntity<?> updatePost(@PathVariable Long postId, @PathVariable Long commentId,
      @RequestBody CommentUpdateDto commentUpdateDto) {
    String email = SpringSecurityUtil.getLoginEmail();
    commentService.updateComment(email, postId, commentId, commentUpdateDto);
    return ResponseEntity.ok("comment updated successfully!");
  }

  // 글 삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
    String email = SpringSecurityUtil.getLoginEmail();
    commentService.deleteComment(email, postId, commentId);
    return ResponseEntity.ok("comment deleted successfully!");
  }

  // 전체 목록 조회
  @GetMapping
  public ResponseEntity<?> getAllComments(
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(commentService.findAllComments(pageable));
  }

}
