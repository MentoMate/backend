package com.example.mentoringproject.post.comment.service;


import com.example.mentoringproject.post.comment.entity.Comment;
import com.example.mentoringproject.post.comment.model.CommentDto;
import com.example.mentoringproject.post.comment.model.CommentRegisterDto;
import com.example.mentoringproject.post.comment.model.CommentUpdateDto;
import com.example.mentoringproject.post.comment.repository.CommentRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  // 댓글 등록
  public void createComment(String email, Long postId, CommentRegisterDto CommentRegisterDto) {
    User user = getUser(email);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    Comment comment = commentRepository.save(Comment.of(user, CommentRegisterDto));
  }

  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Not Found User"));
  }

  // 댓글 수정
  public void updateComment(String email, Long postId, Long commentId, CommentUpdateDto commentUpdateDto) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("Not Found Comment"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new RuntimeException("Not wirter of post");
    }

    comment.setComment(commentUpdateDto.getComment());
    comment.setUpdateDatetime(LocalDateTime.now());

    commentRepository.save(comment);

  }

  // 댓글 삭제
  public void deleteComment(String email, Long postId, Long commentId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("Not Found Comment"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new RuntimeException("Not wirter of post");
    }

    commentRepository.deleteById(commentId);
  }

  // 모든 댓글 조회
  public Page<CommentDto> findAllComments(Pageable pageable) {
    Page<Comment> comments = commentRepository.findAll(pageable);
    List<CommentDto> commentDtos = CommentDto.fromEntity(comments);
    return new PageImpl<>(commentDtos, pageable, comments.getTotalElements());
  }
}
