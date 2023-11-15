package com.example.mentoringproject.user.rating.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.mentee.service.MenteeService;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.model.RatingRequestDto;
import com.example.mentoringproject.mentoring.model.RatingResponseDto;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

  private final UserService userService;
  private final MentoringService mentoringService;
  private final MenteeService menteeService;

  @Transactional
  public RatingResponseDto giveFeedbackAndRating(String email, RatingRequestDto parameter) {
    User user = userService.getUser(email);
    Mentoring mentoring = mentoringService.getMentoring(parameter.getMentoringId());
    List<Mentee> menteeList = menteeService.getMenteeListFromMentoring(mentoring);
    Mentee mentee = checkMenteeListContainsUser(user, menteeList);
    checkAlreadySubmitFeedbackAndRating(mentee);
    mentee.setRating(parameter.getRating());
    mentee.setComment(parameter.getComment());
    return RatingResponseDto.from(mentee, user);
  }

  private void checkAlreadySubmitFeedbackAndRating(Mentee mentee) {
    if (mentee.getRating() != null || mentee.getComment() != null) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이미 평점을 남겼습니다.");
    }
  }

  private static Mentee checkMenteeListContainsUser(User user, List<Mentee> menteeList) {
    return menteeList
        .stream()
        .filter(mentee -> mentee.getUser().equals(user))
        .findFirst()
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "멘토링을 신청한 유저가 아닙니다."));
  }
}
