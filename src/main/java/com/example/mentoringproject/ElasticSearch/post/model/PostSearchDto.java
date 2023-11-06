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
  private String title;
  private String content;
  private String writer;
  private Category category;
  private int postLikesCount;
  private int countWatch;


  public static PostSearchDto fromDocument(PostSearchDocumment post) {
    return PostSearchDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .writer(post.getWriter())
        .category(post.getCategory())
        .postLikesCount(post.getPostLikesCount())
        .countWatch(post.getCountWatch())
        .build();
  }
}