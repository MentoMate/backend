package com.example.mentoringproject.ElasticSearch.mentor.repository;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MentorSearchRepository extends
    ElasticsearchRepository<MentorSearchDocumment, Long> {

  List<MentorSearchDocumment> findAllByNameContainingAndMiddleCategory(String name, String middleCategory);

  List<MentorSearchDocumment> findAll();

  void deleteByName(String name);

  List<MentorSearchDocumment> findAllByNameContaining(String name);

  List<MentorSearchDocumment> findAllByMiddleCategory(String middleCategory);


}