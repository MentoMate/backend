package com.example.mentoringproject.post.post.service;


import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.post.img.entity.Img;
import com.example.mentoringproject.post.img.repository.ImgRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.post.model.PostUpdateRequest;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ImgRepository imgRepository;
  private final S3Service s3Service;

  // 포스팅 등록
  public Post createPost(String email, PostRegisterRequest postRegisterRequest,
      List<MultipartFile> multipartFiles) {
    User user = getUser(email);

    Post post = Post.from(user, postRegisterRequest);

    postRepository.save(post);

    if (multipartFiles != null) {
      List<S3FileDto> s3FileDto = s3Service.upload(multipartFiles, "post", "img");
      Set<Img> ImgList = s3FileDto.stream()
          .map(s3File -> Img.builder()
              .uploadName(s3File.getUploadName())
              .uploadPath(s3File.getUploadPath())
              .uploadUrl(s3File.getUploadUrl())
              .post(post)
              .build())
          .collect(Collectors.toSet());

      imgRepository.saveAll(ImgList);
    }
    return post;
  }


  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User"));
  }

  // 포스팅 수정
  @Transactional
  public Post updatePost(String email, Long postId, PostUpdateRequest postUpdateRequest) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    post.setCategory(postUpdateRequest.getCategory());
    post.setTitle(postUpdateRequest.getTitle());
    post.setContent(postUpdateRequest.getContent());
    post.setUpdateDatetime(LocalDateTime.now());

    postRepository.save(post);

    return post;
  }

  // 포스팅 삭제
  @Transactional
  public void deletePost(String email, Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    List<S3FileDto> s3FileDtoList = post.getImgs().stream()
        .map(mentoringImg -> S3FileDto.builder()
            .uploadName(mentoringImg.getUploadName())
            .uploadPath(mentoringImg.getUploadPath())
            .build())
        .collect(Collectors.toList());
    s3Service.deleteFile(s3FileDtoList);

    postRepository.deleteById(postId);
  }

  // 모든 포스트 조회
  @Transactional(readOnly = true)
  public Page<Post> findAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }
}
