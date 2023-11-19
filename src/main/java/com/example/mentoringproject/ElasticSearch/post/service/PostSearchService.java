package com.example.mentoringproject.ElasticSearch.post.service;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import com.example.mentoringproject.ElasticSearch.post.model.PostSearchDto;
import com.example.mentoringproject.ElasticSearch.post.repository.PostSearchRepository;
import com.example.mentoringproject.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostSearchService {

  private final PostSearchRepository postSearchRepository;
  private final PostRepository postRepository;

  public List<PostSearchDto> searchWriterAndCategory(String writer, String category) {
    List<PostSearchDocumment> postSearchDocummentList = new ArrayList<>();
    if ("default".equals(category)) {
      postSearchDocummentList = postSearchRepository.findAllByWriterContaining(writer);
    } else {
      postSearchDocummentList = postSearchRepository.findAllByWriterContainingAndCategory(writer, category);
    }

    return postSearchDocummentList.stream()
        .map(doc -> PostSearchDto.fromDocument(doc, postRepository))
        .collect(Collectors.toList());
  }

  public List<PostSearchDto> searchTitleAndCategory(String title, String category) {
    List<PostSearchDocumment> postSearchDocummentList = new ArrayList<>();
    if ("default".equals(category)) {
      postSearchDocummentList = postSearchRepository.findAllByTitleContaining(title);
    } else {
      postSearchDocummentList = postSearchRepository.findAllByTitleContainingAndCategory(title, category);
    }
    return postSearchDocummentList.stream()
        .map(doc -> PostSearchDto.fromDocument(doc, postRepository))
        .collect(Collectors.toList());
  }

  public List<PostSearchDto> searchCategory(String category) {
    List<PostSearchDocumment> postSearchDocummentList;

    if ("default".equals(category)) {
      postSearchDocummentList = postSearchRepository.findAll();
    } else {
      postSearchDocummentList = postSearchRepository.findAllByCategory(category);
    }

    return postSearchDocummentList.stream()
        .map(doc -> PostSearchDto.fromDocument(doc, postRepository))
        .collect(Collectors.toList());
  }
}