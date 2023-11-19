package com.example.mentoringproject.ElasticSearch.mentor.service;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentor.model.MentorSearchDto;
import com.example.mentoringproject.ElasticSearch.mentor.repository.MentorSearchRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorSearchService {

  private final MentorSearchRepository mentorSearchRepository;
  private final UserRepository userRepository;


  public List<MentorSearchDto> searchTextAndCategory(String name, String middleCategory,
      String email) {
    List<MentorSearchDocumment> mentorSearchDocuments = new ArrayList<>();
    if ("default".equals(middleCategory)) {
      mentorSearchDocuments = mentorSearchRepository.findAllByNameContaining(name);
    } else {
      mentorSearchDocuments = mentorSearchRepository.findAllByNameContainingAndMiddleCategory(
          name, middleCategory);
    }

    Boolean isMentorRegister = isMentorRegister(email);

    return mentorSearchDocuments.stream()
        .map(doc -> MentorSearchDto.fromDocument(doc, isMentorRegister, userRepository))
        .collect(Collectors.toList());
  }

  public List<MentorSearchDto> searchCategory(String middleCategory, String email) {
    List<MentorSearchDocumment> mentorSearchDocuments = new ArrayList<>();
    if ("default".equals(middleCategory)) {
      mentorSearchDocuments = mentorSearchRepository.findAll();
    } else {
      mentorSearchDocuments = mentorSearchRepository.findAllByMiddleCategory(middleCategory);
    }

    Boolean isMentorRegister = isMentorRegister(email);

    return mentorSearchDocuments.stream()
        .map(doc -> MentorSearchDto.fromDocument(doc, isMentorRegister, userRepository))
        .collect(Collectors.toList());
  }

  public List<MentorSearchDto> searchAll(String email) {
    List<MentorSearchDocumment> mentorSearchDocuments = mentorSearchDocuments = mentorSearchRepository.findAll();

    Boolean isMentorRegister = isMentorRegister(email);

    return mentorSearchDocuments.stream()
        .map(doc -> MentorSearchDto.fromDocument(doc, isMentorRegister, userRepository))
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
