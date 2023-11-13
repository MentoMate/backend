package com.example.mentoringproject.ElasticSearch.mentoring.service;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentoring.model.MentoringSearchDto;
import com.example.mentoringproject.ElasticSearch.mentoring.repository.MentoringSearchRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringSearchService {

  private final MentoringSearchRepository metoringSearchRepository;
  private final UserRepository userRepository;

  public List<MentoringSearchDto> searchTitleAndCategory(String title, String category, String email) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = metoringSearchRepository.findAllByTitleContainingAndCategory(
        title, category);
    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister))
        .collect(Collectors.toList());
  }


  public List<MentoringSearchDto> searchWriterAndCategory(String writer, String category, String email) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = metoringSearchRepository.findAllByWriterAndCategory(
        writer, category);
    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister))
        .collect(Collectors.toList());
  }

  public List<MentoringSearchDto> searchAll(String email) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = metoringSearchRepository.findAll();
    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister))
        .collect(Collectors.toList());
  }

  private boolean isMentorRegister(String email) {
    if (email != null) {
      User user = userRepository.findByEmail(email).orElse(null);

      if (user != null && user.getNickName() != null) {
        return true;
      }
    }
    return false;
  }

}
