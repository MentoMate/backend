package com.example.mentoringproject.ElasticSearch;

import com.example.mentoringproject.ElasticSearch.mentor.repository.MentorSearchRepository;
import com.example.mentoringproject.ElasticSearch.mentoring.repository.MentoringSearchRepository;
import com.example.mentoringproject.ElasticSearch.post.repository.PostSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackageClasses = {PostSearchRepository.class,
    MentoringSearchRepository.class, MentorSearchRepository.class})
@Configuration
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {

  @Override
  public RestHighLevelClient elasticsearchClient() {
    ClientConfiguration clientConfiguration = ClientConfiguration.builder()
        .connectedTo("localhost:9200")
        .build();

    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchOperations elasticsearchOperations() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }


}