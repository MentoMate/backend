package com.example.mentoringproject.mentee.service;

import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.mentee.repository.MenteeRepository;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenteeService {

  private final MenteeRepository menteeRepository;

  public List<User> getUserListFormMentoring(Mentoring mentoring) {
    return menteeRepository.findAllByMentoring(mentoring)
        .stream()
        .map(Mentee::getUser)
        .collect(Collectors.toList());
  }

  public List<Mentee> getMenteeListFromMentoring(Mentoring mentoring) {
    return menteeRepository.findAllByMentoring(mentoring);
  }

  public List<Mentoring> getMentoringListFormMenteeUser(User user) {

    return menteeRepository.findAllByUser(user)
        .stream()
        .map(Mentee::getMentoring)
        .collect(Collectors.toList());
  }
}
