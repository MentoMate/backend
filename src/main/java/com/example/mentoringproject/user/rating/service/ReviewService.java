package com.example.mentoringproject.user.rating.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.mentee.repository.MenteeRepository;
import com.example.mentoringproject.mentee.service.MenteeService;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.rating.model.ReviewRequestDto;
import com.example.mentoringproject.user.rating.model.ReviewResponseDto;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final UserService userService;
  private final MentoringService mentoringService;
  private final MenteeService menteeService;
  private final MentoringRepository mentoringRepository;
  private final MenteeRepository menteeRepository;

  @Transactional
  public ReviewResponseDto giveFeedbackAndRating(String email, ReviewRequestDto parameter) {
    User user = userService.getUser(email);
    Mentoring mentoring = mentoringService.getMentoring(parameter.getMentoringId());
    List<Mentee> menteeList = menteeService.getMenteeListFromMentoring(mentoring);
    Mentee mentee = checkMenteeListContainsUser(user, menteeList);
    checkAlreadySubmitFeedbackAndRating(mentee);
    mentee.setRating(parameter.getRating());
    mentee.setComment(parameter.getComment());
    return ReviewResponseDto.from(mentee, user);
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

  public Page<ReviewResponseDto> getMentorReview(Long mentorId, Pageable pageable) {
    User mentor = userService.getUserById(mentorId);
    List<Mentoring> mentoringList = mentoringRepository.findAllByUser(mentor);
    Page<Mentee> menteePage = menteeRepository.findAllByMentoringInAndRatingIsNotNull(
        mentoringList, pageable);
    return menteePage.map(ReviewResponseDto::from);
  }
}
