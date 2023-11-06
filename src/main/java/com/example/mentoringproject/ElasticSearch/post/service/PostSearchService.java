package com.example.mentoringproject.ElasticSearch.post.service;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.ElasticSearch.post.model.PostSearchDto;
import com.example.mentoringproject.ElasticSearch.post.repository.PostSearchRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostSearchService {

  private final PostSearchRepository postSearchRepository;

  public List<PostSearchDto> searchWriterAndCategory(String writer, String category) {
    List<PostSearchDocumment> postSearchDocummentList = postSearchRepository.findAllByWriterAndCategory (
        writer, category);

    return postSearchDocummentList.stream()
        .map(PostSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

  public List<PostSearchDto> searchTitleAndCategory(String title, String category) {
    List<PostSearchDocumment> postSearchDocummentList = postSearchRepository.findAllByTitleContainingAndCategory(
        title, category);

    return postSearchDocummentList.stream()
        .map(PostSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

  public List<PostSearchDto> searchCategory(String category) {
    List<PostSearchDocumment> postSearchDocummentList = new ArrayList<>();

    if (category.equals("default")){
      postSearchDocummentList = postSearchRepository.findAll();

    } else {
      postSearchDocummentList = postSearchRepository.findAllByCategory(
          category);
    }

    return postSearchDocummentList.stream()
        .map(PostSearchDto::fromDocument)
        .collect(Collectors.toList());
  }

}