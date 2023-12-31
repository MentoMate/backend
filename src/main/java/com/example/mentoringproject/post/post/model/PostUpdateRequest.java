package com.example.mentoringproject.post.post.model;

import com.example.mentoringproject.post.post.entity.Category;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostUpdateRequest {
  private Category category;
  private String title;
  private String content;
  private String uploadFolder;
  private List<String> uploadImg;

}
