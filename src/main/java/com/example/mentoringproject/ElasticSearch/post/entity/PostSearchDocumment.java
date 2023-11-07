package com.example.mentoringproject.ElasticSearch.post.entity;

import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostSearchDocumment {

  @Id
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



  public static PostSearchDocumment fromEntity(Post post) {
    return PostSearchDocumment.builder()
        .id(post.getId())
        .category(post.getCategory())
        .title(post.getTitle())
        .content(post.getContent())
        .writer(post.getUser().getNickName())
        .uploadUrl(post.getUploadUrl())
        .uploadFolder(post.getUploadFolder())
        .postLikesCount(post.getPostLikesCount())
        .commentCount(post.getCommentCount())
        .countWatch(post.getCountWatch())
        .build();
  }
}
