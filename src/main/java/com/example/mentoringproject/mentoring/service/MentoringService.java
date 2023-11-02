package com.example.mentoringproject.mentoring.service;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentoring.repository.MentoringSearchRepository;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.model.MentorByRatingDto;
import com.example.mentoringproject.mentoring.model.MentoringByCountWatchDto;
import com.example.mentoringproject.mentoring.model.MentoringByEndDateDto;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.img.entity.MentoringImg;
import com.example.mentoringproject.mentoring.img.repository.MentoringImgRepository;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostByRegisterDateDto;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import com.example.mentoringproject.user.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
  private final MentoringSearchRepository mentoringSearchRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
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
    
     mentoringSearchRepository.save(MentoringSearchDocumment.fromEntity(user, mentoring));

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

    mentoringSearchRepository.deleteById(mentoringId);
    mentoringSearchRepository.save(MentoringSearchDocumment.fromEntity(user, mentoring));
    
    return mentoringRepository.save(mentoring);

  }
  @Transactional
  public void deleteMentoring(Long mentoringId){

    Mentoring mentoring = getMentoring(mentoringId);

    mentoringSearchRepository.deleteById(mentoringId);

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

  public List<MentoringByCountWatchDto> getMentoringByCountWatch(){
    List<Mentoring> top50MentoringList = mentoringRepository.findTop50ByOrderByCountWatchDesc();
    int totalSize = top50MentoringList.size();

    if (totalSize < 4) {
      return MentoringByCountWatchDto.fromEntity(top50MentoringList);
    }

    Random random = new Random();
    List<Mentoring> randomMentoringList = random.ints(4, 0, totalSize)
        .mapToObj(top50MentoringList::get)
        .collect(Collectors.toList());

    return MentoringByCountWatchDto.fromEntity(randomMentoringList);

  }

  public List<MentorByRatingDto> getMentorByRating() {
    List<User> top50MentorList = userRepository.findTop50ByOrderByRatingDesc();
    int totalSize = top50MentorList.size();

    if (totalSize < 4) {
      return MentorByRatingDto.fromEntity(top50MentorList);
    }

    Random random = new Random();
    List<User> randomMentorList = random.ints(4, 0, totalSize)
        .mapToObj(top50MentorList::get)
        .collect(Collectors.toList());

    return MentorByRatingDto.fromEntity(randomMentorList);
  }

  public List<PostByRegisterDateDto> getPostByRegisterDateTime() {
    List<Post> top50PostList = postRepository.findTop50ByOrderByRegisterDatetimeDesc();
    int totalSize = top50PostList.size();

    if (totalSize < 4) {
      return PostByRegisterDateDto.fromEntity(top50PostList);
    }

    Random random = new Random();
    List<Post> randomPostList = random.ints(4, 0, totalSize)
        .mapToObj(top50PostList::get)
        .collect(Collectors.toList());

    return PostByRegisterDateDto.fromEntity(randomPostList);
  }

  public List<MentoringByEndDateDto> getMentoringByEndDate() {
    LocalDate today = LocalDate.now();
    LocalDate maxEndDate = today.plusDays(5); //

    List<Mentoring> top50MentoringList = mentoringRepository.findByEndDateBetween(today, maxEndDate);
    int totalSize = top50MentoringList.size();

    if (totalSize < 4) {
      return MentoringByEndDateDto.fromEntity(top50MentoringList);
    }

    List<Mentoring> randomMentoringList = getRandomMentorings(top50MentoringList, 4);

    return MentoringByEndDateDto.fromEntity(randomMentoringList);
  }

  private List<Mentoring> getRandomMentorings(List<Mentoring> mentorings, int count) {
    List<Mentoring> randomMentorings = new ArrayList<>();
    Random random = new Random();

    while (randomMentorings.size() < count && !mentorings.isEmpty()) {
      int randomIndex = random.nextInt(mentorings.size());
      randomMentorings.add(mentorings.remove(randomIndex));
    }

    return randomMentorings;
  }





}

