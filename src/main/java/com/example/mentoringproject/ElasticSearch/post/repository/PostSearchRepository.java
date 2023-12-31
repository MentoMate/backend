package com.example.mentoringproject.ElasticSearch.post.repository;

import com.example.mentoringproject.ElasticSearch.post.entity.PostSearchDocumment;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PostSearchRepository extends ElasticsearchRepository<PostSearchDocumment, Long> {

  List<PostSearchDocumment> findAllByWriterContainingAndCategory(String writer, String category);

  List<PostSearchDocumment> findAllByTitleContainingAndCategory(String title, String category);

  List<PostSearchDocumment> findAllByCategory(String category);

  List<PostSearchDocumment> findAll();

  List<PostSearchDocumment> findAllByWriterContaining(String writer);

  List<PostSearchDocumment> findAllByTitleContaining(String title);


}