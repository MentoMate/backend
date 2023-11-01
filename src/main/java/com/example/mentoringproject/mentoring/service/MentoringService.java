package com.example.mentoringproject.mentoring.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.img.entity.MentoringImg;
import com.example.mentoringproject.mentoring.img.repository.MentoringImgRepository;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public Mentoring createMentoring(String email, MentoringDto mentoringDto, List<MultipartFile> multipartFiles){

    User user = userService.profileInfo(email);

    Mentoring mentoring = mentoringRepository.save(Mentoring.from(user, mentoringDto));

    if(multipartFiles != null){
      List<S3FileDto> s3FileDto = s3Service.upload(multipartFiles,"mentoring","img");
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

    List<S3FileDto> s3FileDtoList = mentoring.getMentoringImgList().stream()
        .map(mentoringImg -> S3FileDto.builder()
            .uploadName(mentoringImg.getUploadName())
            .uploadPath(mentoringImg.getUploadPath())
            .build())
        .collect(Collectors.toList());


    s3Service.deleteFile(s3FileDtoList);

    mentoringRepository.delete(mentoring);
  }

  @Transactional
  public Mentoring mentoringInfo(Long mentoringId){
      mentoringRepository.updateCount(mentoringId);

    return  getMentoring(mentoringId);
  }

  public Mentoring getMentoring(Long mentoringId){
    return mentoringRepository.findById(mentoringId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "존재 하지 않는 멘토링 입니다."));
  }
}
