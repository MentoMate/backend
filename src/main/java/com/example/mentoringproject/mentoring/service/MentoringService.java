package com.example.mentoringproject.mentoring.service;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringService {
  private final MentoringRepository mentoringRepository;
  private final UserService userService;

    public void createMentoring(String email, MentoringDto mentoringDto){

      User user = userService.getUser(email);

      //프로필 등록여부 확인

      mentoringRepository.save( Mentoring.from(user, mentoringDto));
    }

  @Transactional
  public void updateMentoring(Long mentoringId, MentoringDto mentoringDto){

    Mentoring mentoring = getMentoring(mentoringId);

    mentoring.setTitle(mentoringDto.getTitle());
    mentoring.setContent(mentoringDto.getContent());
    mentoring.setStartDate(mentoringDto.getStartDate());
    mentoring.setEndDate(mentoringDto.getEndDate());
    mentoring.setNumberOfPeople(mentoringDto.getNumberOfPeople());
    mentoring.setAmount(mentoringDto.getAmount());
    mentoring.setCategory(mentoringDto.getCategory());
    mentoring.setImgUrl(mentoringDto.getImgUrl());

    mentoringRepository.save(mentoring);
  }
  @Transactional
  public void deleteMentoring(Long mentoringId){

    Mentoring mentoring = getMentoring(mentoringId);
    mentoring.setStatus(MentoringStatus.DELETE);

    mentoringRepository.save(mentoring);
  }

  @Transactional
  public MentoringInfo MentoringInfo(Long mentoringId){
      mentoringRepository.updateCount(mentoringId);

      Mentoring mentoring = getMentoring(mentoringId);

    return  MentoringInfo.from(mentoring);
  }

  public Mentoring getMentoring(Long mentoringId){
    Mentoring mentoring = mentoringRepository.findById(mentoringId).orElseThrow(
        () -> new RuntimeException("존재 하지 않는 멘토링 입니다."));

    if(mentoring.getStatus() == MentoringStatus.DELETE) throw new RuntimeException("삭제된 멘토링 입니다.");

    return  mentoring;
  }
}
