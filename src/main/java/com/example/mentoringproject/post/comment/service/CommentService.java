package com.example.mentoringproject.post.comment.service;


import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.comment.model.CommentDto;
import com.example.mentoringproject.post.comment.model.CommentRegisterRequest;
import com.example.mentoringproject.post.comment.model.CommentUpdateRequest;
import com.example.mentoringproject.post.comment.repository.CommentRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  // 댓글 등록
  public Comment createComment(String email, Long postId, CommentRegisterRequest commentRegisterRequest) {
    User user = getUser(email);

    Post post = postRepository.findById(postId)
        .orElseThrow(() ->  new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    Comment comment = commentRepository.save(Comment.from(user, post, commentRegisterRequest));

    post.setCommentCount(post.getCommentCount()+1);

    int count = post.getCommentCount();

    commentRepository.save(comment);

    return comment;
  }

  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User"));
  }

  // 댓글 수정
  @Transactional
  public Comment updateComment(String email, Long postId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Comment"));

    if (!comment.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of comment");
    }

    comment.setComment(commentUpdateRequest.getComment());
    comment.setUpdateDatetime(LocalDateTime.now());

    commentRepository.save(comment);

    return comment;

  }

  // 댓글 삭제
  @Transactional
  public void deleteComment(String email, Long postId, Long commentId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Comment"));

    if (!comment.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of comment");
    }

    post.setCommentCount(post.getCommentCount() - 1);

    commentRepository.deleteById(commentId);
  }

  // 모든 댓글 조회
  @Transactional(readOnly = true)
  public Page<CommentDto> findAllCommentsByPostId(Long postId, Pageable pageable, String email) {

    Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);

    List<CommentDto> commentDtos = commentsPage.getContent().stream()
        .map(comment -> CommentDto.fromEntity(comment, email.equals(comment.getUser().getEmail())))
        .collect(Collectors.toList());

    return new PageImpl<>(commentDtos, pageable, commentsPage.getTotalElements());
  }

}
