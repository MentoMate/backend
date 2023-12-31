package com.example.mentoringproject.ElasticSearch.mentoring.service;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentoring.model.MentoringSearchDto;
import com.example.mentoringproject.ElasticSearch.mentoring.repository.MentoringSearchRepository;
import com.example.mentoringproject.mentoring.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringSearchService {

  private final MentoringSearchRepository metoringSearchRepository;
  private final MentoringRepository mentoringRepository;
  private final UserRepository userRepository;

  public List<MentoringSearchDto> searchTitleAndCategory(String title, String category, String email) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = new ArrayList<>();
    if ("default".equals(category)) {
      mentoringSearchDocuments = metoringSearchRepository.findAllByTitleContaining(title);
    } else {
      mentoringSearchDocuments = metoringSearchRepository.findAllByTitleContainingAndCategory(title, category);
    }

    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister, mentoringRepository))
        .collect(Collectors.toList());
  }


  public List<MentoringSearchDto> searchWriterAndCategory(String writer, String category, String email) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = new ArrayList<>();
    if ("default".equals(category)) {
      mentoringSearchDocuments = metoringSearchRepository.findAllByWriterContaining(writer);
    } else {
      mentoringSearchDocuments = metoringSearchRepository.findAllByWriterContainingAndCategory(
          writer, category);
    }

    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister, mentoringRepository))
        .collect(Collectors.toList());
  }

  public List<MentoringSearchDto> searchAllByCategory(String email, String category) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = new ArrayList<>();
    if ("default".equals(category)) {
      mentoringSearchDocuments = metoringSearchRepository.findAll();
    } else {
      mentoringSearchDocuments = metoringSearchRepository.findAllByCategory(category);
    }
    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister, mentoringRepository))
        .collect(Collectors.toList());
  }

  public List<MentoringSearchDto> searchAll(String email) {
    List<MentoringSearchDocumment> mentoringSearchDocuments = metoringSearchRepository.findAll();

    Boolean isMentorRegister = isMentorRegister(email);

    return mentoringSearchDocuments.stream()
        .map(doc -> MentoringSearchDto.fromDocument(doc, isMentorRegister, mentoringRepository))
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
