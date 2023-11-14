package com.example.mentoringproject.ElasticSearch.post.model;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostSearchDto {

  private Long id;
  private Category category;
  private String title;
  private String content;
  private String writer;
  private String uploadUrl;
  private String uploadFolder;
  private int postLikesCount;
  private int commentCount;


  public static PostSearchDto fromDocument(PostSearchDocumment postSearchDocumment, PostRepository postRepository) {
    Post post = postRepository.findById(postSearchDocumment.getId()).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST,"Not found Post"));
    int postLikesCount = post != null ? post.getPostLikesCount() : 0;
    int commentCount = post != null ? post.getCommentCount() : 0;
    return PostSearchDto.builder()
        .id(postSearchDocumment.getId())
        .category(postSearchDocumment.getCategory())
        .title(postSearchDocumment.getTitle())
        .content(postSearchDocumment.getContent())
        .writer(postSearchDocumment.getWriter())
        .uploadUrl(postSearchDocumment.getUploadUrl())
        .uploadFolder(postSearchDocumment.getUploadFolder())
        .postLikesCount(postLikesCount)
        .commentCount(commentCount)
        .build();
  }
}
