package com.example.mentoringproject.post.comment.controller;

import com.example.mentoringproject.ElasticSearch.util.SearchResult;
import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.post.comment.model.CommentDto;
import com.example.mentoringproject.post.comment.model.CommentRegisterRequest;
import com.example.mentoringproject.post.comment.model.CommentUpdateRequest;
import com.example.mentoringproject.post.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게시판-댓글", description = "comment API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  // 댓글 등록
  @Operation(summary = "댓글 등록 api", description = "댓글 등록 api", responses = {
      @ApiResponse(responseCode = "200", description = "댓글 등록 성공", content =
      @Content(schema = @Schema(implementation = CommentDto.class)))
  })
  @PostMapping
  public ResponseEntity<CommentDto> createComment(@PathVariable Long postId,
      @RequestBody CommentRegisterRequest commentRegisterRequest) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(
        CommentDto.fromEntity(commentService.createComment(email, postId, commentRegisterRequest)));
  }

  // 댓글 수정
  @Operation(summary = "댓글 수정 api", description = "댓글 수정 api", responses = {
      @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content =
      @Content(schema = @Schema(implementation = CommentDto.class)))
  })
  @PutMapping("/{commentId}")
  public ResponseEntity<CommentDto> updatePost(@PathVariable Long postId,
      @PathVariable Long commentId,
      @RequestBody CommentUpdateRequest commentUpdateRequest) {
    String email = SpringSecurityUtil.getLoginEmail();

    return ResponseEntity.ok(CommentDto.fromEntity(
        commentService.updateComment(email, postId, commentId, commentUpdateRequest)));
  }

  // 댓글 삭제
  @Operation(summary = "댓글 삭제 api", description = "댓글 삭제 api", responses = {
      @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
  })
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
      @PathVariable Long commentId) {
    String email = SpringSecurityUtil.getLoginEmail();
    commentService.deleteComment(email, postId, commentId);
    return ResponseEntity.ok().build();
  }

  // 전체 댓글 조회
  @Operation(summary = "댓글 조회 api", description = "댓글 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content =
      @Content(schema = @Schema(implementation = CommentDto.class)))
  })

  @GetMapping
  public ResponseEntity<SearchResult<CommentDto>> getAllComments(
      @PathVariable Long postId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "8") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    String email = SpringSecurityUtil.getLoginEmail();
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.fromString(sortDirection), sortBy);

    Page<CommentDto> commentsPage = commentService.findAllCommentsByPostId(postId, pageable, email);

    SearchResult<CommentDto> searchResult = new SearchResult<>(
        commentsPage.getTotalPages(),
        commentsPage.getContent()
    );

    return ResponseEntity.ok(searchResult);
  }

}
