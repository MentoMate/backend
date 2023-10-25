package com.example.mentoringproject.mentoring.service;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringService {
  private final MentoringRepository mentoringRepository;
  private final UserService userService;

    public void createMentoring(String token, MentoringDto mentoringDto){
      User user = userService.getUser(token);

      //프로필 등록여부 확인

      mentoringRepository.save(Mentoring.builder()
              .title(mentoringDto.getTitle())
              .content(mentoringDto.getContent())
              .startDate(mentoringDto.getStartDate())
              .endDate(mentoringDto.getEndDate())
              .numberOfPeople(mentoringDto.getNumberOfPeople())
              .amount(mentoringDto.getAmount())
              .status(mentoringDto.getStatus())
              .category(mentoringDto.getCategory())
              .imgUrl(mentoringDto.getImgUrl())
              .registerDate(LocalDateTime.now())
              .user(user)
              .build()
      );
    }

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
    mentoring.setUpdateDate(LocalDateTime.now());

    mentoringRepository.save(mentoring);
  }

  public void deleteMentoring(Long mentoringId){

    Mentoring mentoring = getMentoring(mentoringId);

    mentoring.setStatus(MentoringStatus.DELETE);
    mentoring.setDeleteDate(LocalDateTime.now());

    mentoringRepository.save(mentoring);
  }

  public MentoringInfo MentoringInfo(Long mentoringId){
      Mentoring mentoring = getMentoring(mentoringId);

      mentoring.setCountWatch(mentoring.getCountWatch()+1);
      mentoringRepository.save(mentoring);

    return  MentoringInfo.from(mentoring);
  }

  public Mentoring getMentoring(Long mentoringId){
    Mentoring mentoring = mentoringRepository.findById(mentoringId).orElseThrow(
        () -> new RuntimeException("존재 하지 않는 멘토링 입니다."));

    if(mentoring.getStatus() == MentoringStatus.DELETE) throw new RuntimeException("삭제된 멘토링 입니다.");

    return  mentoring;
  }
}
