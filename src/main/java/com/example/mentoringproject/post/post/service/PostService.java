package com.example.mentoringproject.post.post.service;


import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.post.img.entity.Img;
import com.example.mentoringproject.post.img.repository.ImgRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostDto;
import com.example.mentoringproject.post.post.model.PostRegisterDto;
import com.example.mentoringproject.post.post.model.PostUpdateDto;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
  public void createPost(String email, PostRegisterDto postRegisterDto,
      List<MultipartFile> multipartFiles) {
    User user = getUser(email);

    Post post = Post.from(user, postRegisterDto);

    postRepository.save(post); // 엔티티를 저장하고 반환

    if(multipartFiles != null) {
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
  }


  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Not Found User"));
  }

  // 포스팅 수정
  @Transactional
  public void updatePost(String email, Long postId, PostUpdateDto postUpdateDto) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new RuntimeException("Not wirter of post");
    }

    post.setCategory(postUpdateDto.getCategory());
    post.setTitle(postUpdateDto.getTitle());
    post.setContent(postUpdateDto.getContent());
    //post.setImgUrl(postUpdateDto.getImgUrl());
    post.setUpdateDatetime(LocalDateTime.now());

    postRepository.save(post);

  }

  // 포스팅 삭제
  @Transactional
  public void deletePost(String email, Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new RuntimeException("Not wirter of post");
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
  public Page<PostDto> findAllPosts(Pageable pageable) {
    Page<Post> posts = postRepository.findAll(pageable);
    List<PostDto> postDtos = PostDto.fromEntity(posts);
    return new PageImpl<>(postDtos, pageable, posts.getTotalElements());
  }


}
