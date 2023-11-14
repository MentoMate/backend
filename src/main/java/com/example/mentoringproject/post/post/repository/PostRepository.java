package com.example.mentoringproject.post.post.repository;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  @Modifying
  @Query("update post set countWatch = countWatch + 1 where id = :id")
  void updateCount(@Param("id") Long id);

  Optional<Post> findById(String postId);
  List<Post> findTop50ByCategoryOrderByRegisterDatetimeDesc(Category category);

  Page<Post> findByUserId(Long postId, Pageable pageable);


}
