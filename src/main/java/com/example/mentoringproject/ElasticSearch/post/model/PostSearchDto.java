package com.example.mentoringproject.ElasticSearch.post.model;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.post.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private int countWatch;


  public static PostSearchDto fromDocument(PostSearchDocumment post) {
    return PostSearchDto.builder()
        .id(post.getId())
        .category(post.getCategory())
        .title(post.getTitle())
        .content(post.getContent())
        .writer(post.getWriter())
        .uploadUrl(post.getUploadUrl())
        .uploadFolder(post.getUploadFolder())
        .postLikesCount(post.getPostLikesCount())
        .commentCount(post.getCommentCount())
        .countWatch(post.getCountWatch())
        .build();
  }
}
