package com.example.mentoringproject.user.model;


import com.example.mentoringproject.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Getter
@Builder
public class UserProfileSave {
  private Long userId;
  private String name;
  private int career;
  private String introduce;
  private String mainCategory;
  private String middleCategory;
  private String uploadFolder;
  private List<String> uploadImg;
}
