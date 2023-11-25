package com.example.mentoringproject.mentee.service;

import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.mentee.repository.MenteeRepository;
import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

  public List<Mentoring> getEndedAndWithoutRatingInputMentoring(User user) {
    return menteeRepository.findAllByUserAndRatingIsNull(user)
            .stream()
            .map(Mentee::getMentoring)
            .collect(Collectors.toList());
  }


  public void deleteMenteeList(List<Mentee> menteeList){
    menteeRepository.deleteAllInBatch(menteeList);
  }
  
  public Mentee addMentee(User mentee, Mentoring mentoring) {
    return menteeRepository.save(Mentee.builder()
        .mentoring(mentoring)
        .user(mentee)
        .build());

  }
}
