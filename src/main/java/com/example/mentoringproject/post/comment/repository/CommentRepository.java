package com.example.mentoringproject.post.comment.repository;

import com.example.mentoringproject.post.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Optional<Comment> findById(Long commentId);

  Page<Comment> findByPostId(Long postId, Pageable pageable);

}
