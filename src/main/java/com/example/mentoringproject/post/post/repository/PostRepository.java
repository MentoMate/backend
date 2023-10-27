package com.example.mentoringproject.post.post.repository;

import com.example.mentoringproject.post.post.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findById(String postId);

}