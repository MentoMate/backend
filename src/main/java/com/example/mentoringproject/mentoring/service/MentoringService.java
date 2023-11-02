package com.example.mentoringproject.mentoring.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.img.entity.MentoringImg;
import com.example.mentoringproject.mentoring.img.repository.MentoringImgRepository;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringService {
  private final MentoringRepository mentoringRepository;
  private final MentoringImgRepository mentoringImgRepository;
  private final UserService userService;
  private final S3Service s3Service;
  @Transactional
  public Mentoring createMentoring(String email, MentoringDto mentoringDto, List<MultipartFile> thumbNailImg, List<MultipartFile> multipartFiles){

    User user = userService.profileInfo(userService.getUser(email).getId());

    List<S3FileDto> s3FileDto = s3Service.upload(thumbNailImg,"mentoring","img");
    mentoringDto.setUploadPath(s3FileDto.get(0).getUploadPath());
    mentoringDto.setUploadName(s3FileDto.get(0).getUploadName());
    mentoringDto.setUploadUrl(s3FileDto.get(0).getUploadUrl());
    mentoringDto.setStatus(MentoringStatus.PROGRESS);

    Mentoring mentoring = mentoringRepository.save(Mentoring.from(user, mentoringDto));


    if(multipartFiles != null){
      s3FileDto = s3Service.upload(multipartFiles,"mentoring","img");
      Set<MentoringImg> mentoringImgList = s3FileDto.stream()
          .map(s3File -> MentoringImg.builder()
              .mentoring(mentoring)
              .uploadName(s3File.getUploadName())
              .uploadPath(s3File.getUploadPath())
              .uploadUrl(s3File.getUploadUrl())
              .build())
          .collect(Collectors.toSet());

      mentoringImgRepository.saveAll(mentoringImgList);
    }

    return  mentoring;
  }

  @Transactional
  public Mentoring updateMentoring(Long mentoringId, MentoringDto mentoringDto){

    Mentoring mentoring = getMentoring(mentoringId);

    mentoring.setTitle(mentoringDto.getTitle());
    mentoring.setContent(mentoringDto.getContent());
    mentoring.setStartDate(mentoringDto.getStartDate());
    mentoring.setEndDate(mentoringDto.getEndDate());
    mentoring.setNumberOfPeople(mentoringDto.getNumberOfPeople());
    mentoring.setAmount(mentoringDto.getAmount());
    mentoring.setCategory(mentoringDto.getCategory());

    return mentoringRepository.save(mentoring);
  }
  @Transactional
  public void deleteMentoring(Long mentoringId){

    Mentoring mentoring = getMentoring(mentoringId);

    mentoring.setStatus(MentoringStatus.DELETE);
    mentoring.setDeleteDate(LocalDateTime.now());

    mentoringRepository.save(mentoring);
  }

  @Transactional
  public Mentoring mentoringInfo(Long mentoringId){
      mentoringRepository.updateCount(mentoringId);

    return  getMentoring(mentoringId);
  }

  public Mentoring getMentoring(Long mentoringId){
    return mentoringRepository.findById(mentoringId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "존재 하지 않는 멘토링 입니다."));
  }

  @Transactional(readOnly = true)
  public Page<Mentoring> getMentoringList(Pageable pageable) {
    return mentoringRepository.findByStatus(MentoringStatus.PROGRESS, pageable);
  }
}
