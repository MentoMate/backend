package com.example.mentoringproject.post.post.service;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.ElasticSearch.post.repository.PostSearchRepository;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostInfoResponseDto;
import com.example.mentoringproject.post.post.model.PostMyPageResponse;
import com.example.mentoringproject.post.post.model.PostRegisterRequest;
import com.example.mentoringproject.post.post.model.PostUpdateRequest;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import com.example.mentoringproject.user.user.service.UserService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
  private final UserService userService;
  private final PostSearchRepository postSearchRepository;
  private final S3Service s3Service;

  private static final String FOLDER = "post/";
  private static final String FILE_TYPE = "img";

  // 포스팅 등록
  public Post createPost(String email, PostRegisterRequest postRegisterRequest,
      List<MultipartFile> thumbNailImg) {
    User user = getUser(email);

    Post post = Post.from(user, postRegisterRequest);

    String uploadPath = FOLDER + postRegisterRequest.getUploadFolder();
    List<S3FileDto> s3FileDto = s3Service.upload(thumbNailImg, uploadPath, FILE_TYPE);
    post.setUploadUrl(s3FileDto.get(0).getUploadUrl());

    postRepository.save(post);

    postSearchRepository.save(PostSearchDocumment.fromEntity(post));

    List<String> imgList = Optional.ofNullable(postRegisterRequest.getUploadImg())
        .orElse(Collections.emptyList())
        .stream()
        .map(s3Service::extractFileName)
        .collect(Collectors.toList());
    imgList.add(s3Service.extractFileName(post.getUploadUrl()));
    s3Service.fileClear(FOLDER + postRegisterRequest.getUploadFolder(), imgList);

    return post;
  }


  private User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found User"));
  }

  // 포스팅 수정
  @Transactional
  public Post updatePost(String email, Long postId, PostUpdateRequest postUpdateRequest,
      List<MultipartFile> thumbNailImg) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Not Found Post"));

    if (!post.getUser().getEmail().equals(email)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Not Writer of Post");
    }

    post.setCategory(postUpdateRequest.getCategory());
    post.setTitle(postUpdateRequest.getTitle());
    post.setContent(postUpdateRequest.getContent());
    post.setUpdateDatetime(LocalDateTime.now());

    String uploadPath = FOLDER + postUpdateRequest.getUploadFolder();
    List<S3FileDto> s3FileDto = s3Service.upload(thumbNailImg, uploadPath, FILE_TYPE);
    post.setUploadUrl(s3FileDto.get(0).getUploadUrl());

    postRepository.save(post);

    postSearchRepository.save(PostSearchDocumment.fromEntity(post));

    List<String> imgList = Optional.ofNullable(postUpdateRequest.getUploadImg())
        .orElse(Collections.emptyList())
        .stream()
        .map(s3Service::extractFileName)
        .collect(Collectors.toList());
    imgList.add(s3Service.extractFileName(post.getUploadUrl()));
    s3Service.fileClear(FOLDER + postUpdateRequest.getUploadFolder(), imgList);

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

    s3Service.fileClear(FOLDER + post.getUploadFolder(), Collections.emptyList());
    postRepository.deleteById(postId);
    postSearchRepository.deleteById(postId);
  }

  @Transactional
  public PostInfoResponseDto postInfo(String email, Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "존재 하지 않는 글 입니다."));

    boolean isOwner = false;

    if (post.getUser().getEmail().equals(email)) {
      isOwner = true;
    }

    boolean isLike = post.getPostLikes().stream()
        .anyMatch(postLikes -> postLikes.getUser().getEmail().equals(email));


    postRepository.updateCount(postId);
    return PostInfoResponseDto.fromEntity(post, isOwner, isLike);
  }

  @Transactional
  public Page<PostMyPageResponse> getPostMyPage(String email, Pageable pageable){
    User user = userService.getUser(email);
    return postRepository.findByUserId(user.getId(), pageable)
        .map(PostMyPageResponse::fromEntity);
  }

}
