package com.example.mentoringproject.post.service;


import com.example.mentoringproject.post.entity.Post;
import com.example.mentoringproject.post.model.PostDto;
import com.example.mentoringproject.post.model.PostRegisterDto;
import com.example.mentoringproject.post.model.PostUpdateDto;
import com.example.mentoringproject.post.repository.PostRepository;
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
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 포스팅 등록
  public void createPost(String email, PostRegisterDto postRegisterDto) {
    User user = getUser(email);
    Post post = postRepository.save(Post.of(user, postRegisterDto));
  }

  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Not Found User"));
  }

  // 포스팅 수정
  public void updatePost(String email, Long postId, PostUpdateDto postUpdateDto) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new RuntimeException("Not wirter of post");
    }

    post.setCategory(postUpdateDto.getCategory());
    post.setTitle(postUpdateDto.getTitle());
    post.setContent(postUpdateDto.getContent());
    post.setImgUrl(postUpdateDto.getImgUrl());
    post.setUpdateDatetime(LocalDateTime.now());

    postRepository.save(post);

  }

  // 포스팅 삭제
  public void deletePost(String email, Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new RuntimeException("Not wirter of post");
    }
    postRepository.deleteById(postId);
  }

  // 모든 포스트 조회
  public Page<PostDto> findAllPosts(Pageable pageable) {
    Page<Post> posts = postRepository.findAll(pageable);
    List<PostDto> postDtos = PostDto.fromEntity(posts);
    return new PageImpl<>(postDtos, pageable, posts.getTotalElements());
  }



}
