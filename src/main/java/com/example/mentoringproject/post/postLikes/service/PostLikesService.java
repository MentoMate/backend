package com.example.mentoringproject.post.postLikes.service;


import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.post.postLikes.entity.PostLikes;
import com.example.mentoringproject.post.postLikes.repository.PostLikesRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikesService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostLikesRepository postLikesRepository;

  // 좋아요 등록 / 취소
  @Transactional
  public Optional<PostLikes> switchPostLikes(String email, Long postId) {
    User user = getUser(email);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));
    Optional<PostLikes> postLikes = postLikesRepository.findByUserAndPost(user, post);
    if (postLikes.isEmpty()) {
      PostLikes likes = PostLikes.from(user, post);
      postLikesRepository.save(likes);
      post.setPostLikesCount(post.getPostLikesCount() + 1);
      return Optional.of(likes);
    } else {
      postLikesRepository.delete(postLikes.get());
      post.setPostLikesCount(post.getPostLikesCount() - 1);
      return Optional.empty();
    }
  }


  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User"));
  }

}
