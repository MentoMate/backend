package com.example.mentoringproject.mentoring.service;

import com.example.mentoringproject.ElasticSearch.mentoring.entity.MentoringSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentoring.repository.MentoringSearchRepository;
import com.example.mentoringproject.chat.repository.PrivateChatRoomRepository;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.mentee.service.MenteeService;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.model.CountDto;
import com.example.mentoringproject.mentoring.model.MentorByRatingDto;
import com.example.mentoringproject.mentoring.model.MentoringByCountWatchDto;
import com.example.mentoringproject.mentoring.model.MentoringByEndDateDto;
import com.example.mentoringproject.mentoring.model.MentoringDto;
import com.example.mentoringproject.mentoring.model.MentoringInfo;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.pay.entity.PayStatus;
import com.example.mentoringproject.pay.service.PayService;
import com.example.mentoringproject.post.post.entity.Category;
import com.example.mentoringproject.post.post.entity.Post;
import com.example.mentoringproject.post.post.model.PostByRegisterDateDto;
import com.example.mentoringproject.post.post.repository.PostRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import com.example.mentoringproject.user.user.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MentoringService {

  private final MentoringRepository mentoringRepository;
  private final MentoringSearchRepository mentoringSearchRepository;
  private final MenteeService menteeService;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PrivateChatRoomRepository privateChatRoomRepository;
  private final UserService userService;
  private final PayService payService;
  private final S3Service s3Service;

  private static final String FOLDER = "mentoring/";
  private static final String FILE_TYPE = "img";

  @Transactional
  public Mentoring createMentoring(String email, MentoringSave mentoringSave, List<MultipartFile> thumbNailImg){

    User user = userService.profileInfo(userService.getUser(email).getId());

    Mentoring mentoring = Mentoring.from(user, mentoringSave);
    mentoring.setStatus(MentoringStatus.PROGRESS);

    imgUpload(mentoringSave, mentoring, thumbNailImg);

    mentoringRepository.save(mentoring);

    mentoringSearchRepository.save(MentoringSearchDocumment.fromEntity(user, mentoring));

    return mentoring;
  }

  @Transactional
  public Mentoring updateMentoring(String email, MentoringSave mentoringSave, List<MultipartFile> thumbNailImg){

    Mentoring mentoring = getMentoring(mentoringSave.getMentoringId());
    User user = userService.profileInfo(userService.getUser(email).getId());

    mentoring.setTitle(mentoringSave.getTitle());
    mentoring.setContent(mentoringSave.getContent());
    mentoring.setStartDate(mentoringSave.getStartDate());
    mentoring.setEndDate(mentoringSave.getEndDate());
    mentoring.setNumberOfPeople(mentoringSave.getNumberOfPeople());
    mentoring.setAmount(mentoringSave.getAmount());
    mentoring.setCategory(mentoringSave.getCategory());



    imgUpload(mentoringSave, mentoring, thumbNailImg);

    mentoringRepository.save(mentoring);

    mentoringSearchRepository.save(MentoringSearchDocumment.fromEntity(user, mentoring));

    return mentoring;

  }


  public void deleteMentoring(Long mentoringId, String restApiKey, String restApiSecret){

    Mentoring mentoring = getMentoring(mentoringId);

    if(!mentoring.getStatus().equals(MentoringStatus.PROGRESS)){
      throw new AppException(HttpStatus.BAD_REQUEST, "시작한 멘토링은 삭제할 수 없습니다.");
    }

    List<Mentee> menteeList = menteeService.getMenteeListFromMentoring(mentoring);
    if(!menteeList.isEmpty()){
      payService.payCancelByMentor(menteeList, mentoring.getId(), restApiKey,restApiSecret);
    }
    menteeService.deleteMenteeList(menteeList);
    mentoring.setStatus(MentoringStatus.DELETE);
    mentoring.setDeleteDate(LocalDateTime.now());

    mentoringRepository.save(mentoring);
    mentoringSearchRepository.deleteById(mentoringId);
  }

  @Transactional
  public MentoringInfo mentoringInfo(String email, Long mentoringId){

    Mentoring mentoring = getMentoring(mentoringId);
    boolean isOwner = false;
    if (mentoring.getUser().getEmail().equals(email)) {
      isOwner = true;
    }

    User user = userService.getUser(email);

    boolean isPrivateChatRoomCreate = false;
    if (privateChatRoomRepository.existsByUserIdAndMentoringId(user.getId(), mentoringId)) {
      isPrivateChatRoomCreate = true;
    }

    mentoringRepository.updateCount(mentoringId);
    List<User> userList = menteeService.getUserListFormMentoring(mentoring);
    return MentoringInfo.from(mentoring, isOwner, isPrivateChatRoomCreate, userList);
  }

  public Mentoring getMentoring(Long mentoringId){
    Mentoring mentoring = mentoringRepository.findById(mentoringId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "존재 하지 않는 멘토링 입니다."));
    if(mentoring.getStatus() == MentoringStatus.DELETE) throw  new AppException(HttpStatus.BAD_REQUEST, "삭제된 멘토링 입니다.");
    return  mentoring;
  }


  public List<MentoringByCountWatchDto> getMentoringByCountWatch() {
    List<Mentoring> top50MentoringList = mentoringRepository.findTop50ByOrderByCountWatchDesc();
    int totalSize = top50MentoringList.size();

    if (totalSize < 4) {
      return MentoringByCountWatchDto.fromEntity(top50MentoringList);
    }

    Random random = new Random();
    Set<Integer> randomIndices = new HashSet<>();

    while (randomIndices.size() < 4) {
      randomIndices.add(random.nextInt(totalSize));
    }

    List<Mentoring> randomMentoringList = randomIndices.stream()
        .map(top50MentoringList::get)
        .collect(Collectors.toList());

    return MentoringByCountWatchDto.fromEntity(randomMentoringList);
  }


  public List<MentorByRatingDto> getMentorByRating() {
    List<User> top50MentorList = userRepository.findTop50ByOrderByRatingDesc()
        .stream()
        .filter(user -> user.getName() != null)
        .collect(Collectors.toList());

    int totalSize = top50MentorList.size();

    if (totalSize < 4) {
      return MentorByRatingDto.fromEntity(top50MentorList);
    }

    Random random = new Random();
    Set<Integer> randomIndices = new HashSet<>();

    while (randomIndices.size() < 4) {
      randomIndices.add(random.nextInt(totalSize));
    }

    List<User> randomMentorList = randomIndices.stream()
        .map(top50MentorList::get)
        .collect(Collectors.toList());

    return MentorByRatingDto.fromEntity(randomMentorList);
  }

  public List<PostByRegisterDateDto> getPostByRegisterDateTime() {
    List<Post> top50PostList = postRepository.findTop50ByCategoryOrderByRegisterDatetimeDesc(Category.review);
    int totalSize = top50PostList.size();

    if (totalSize < 4) {
      return PostByRegisterDateDto.fromEntity(top50PostList);
    }

    Random random = new Random();
    Set<Integer> randomIndices = new HashSet<>();

    while (randomIndices.size() < 4) {
      randomIndices.add(random.nextInt(totalSize));
    }

    List<Post> randomPostList = randomIndices.stream()
        .map(top50PostList::get)
        .collect(Collectors.toList());

    return PostByRegisterDateDto.fromEntity(randomPostList);
  }


  public List<MentoringByEndDateDto> getMentoringByEndDate() {
    LocalDate today = LocalDate.now();
    LocalDate maxEndDate = today.plusDays(3);

    List<Mentoring> top50MentoringList = mentoringRepository.findByEndDateBetween(today,
        maxEndDate);
    int totalSize = top50MentoringList.size();

    if (totalSize < 4) {
      return MentoringByEndDateDto.fromEntity(top50MentoringList);
    }

    Random random = new Random();
    Set<Integer> randomIndices = new HashSet<>();

    while (randomIndices.size() < 4) {
      randomIndices.add(random.nextInt(totalSize));
    }

    List<Mentoring> randomMentoringList = randomIndices.stream()
        .map(top50MentoringList::get)
        .collect(Collectors.toList());

    return MentoringByEndDateDto.fromEntity(randomMentoringList);
  }


  @Transactional
  public void deleteMentoringUserByCancelPayment(Pay pay) {
    Mentoring mentoring = pay.getMentoring();
    List<User> userList = menteeService.getUserListFormMentoring(mentoring);
    userList.removeIf(user -> user.equals(pay.getUser()));
  }

  private void imgUpload(MentoringSave mentoringSave, Mentoring mentoring, List<MultipartFile> thumbNailImg){
    String uploadPath = FOLDER + mentoringSave.getUploadFolder();
    List<S3FileDto> s3FileDto = s3Service.upload(thumbNailImg,uploadPath,FILE_TYPE);
    mentoring.setUploadUrl(s3FileDto.get(0).getUploadUrl());

    List<String> imgList = Optional.ofNullable(mentoringSave.getUploadImg())
        .orElse(Collections.emptyList())
        .stream()
        .map(s3Service::extractFileName)
        .collect(Collectors.toList());
    imgList.add(s3Service.extractFileName(mentoring.getUploadUrl()));

    s3Service.fileClear(uploadPath, imgList);
  }

  public List<CountDto> getCount() {
    List<CountDto> countDtoList = new ArrayList<>();

    CountDto countDto = new CountDto();
    countDto.setMentoringCount(mentoringRepository.count());
    countDto.setMentorCount(userRepository.countByNameIsNotNull());
    countDtoList.add(countDto);

    return countDtoList;
  }

  public Page<MentoringDto> getEndedMentoringList(String email, Pageable pageable) {
    User user = userService.getUser(email);
    List<MentoringDto> mentoringDtoList = menteeService.getMentoringListFormMenteeUser(user)
        .stream()
        .filter(mentoring -> mentoring.getStatus().equals(MentoringStatus.FINISH))
        .map(MentoringDto::from)
        .collect(Collectors.toList());

    return pagingMentoringDto(pageable, mentoringDtoList);
  }

  private static Page<MentoringDto> pagingMentoringDto(Pageable pageable, List<MentoringDto> mentoringDtoList) {
    PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageable.getPageSize()), mentoringDtoList.size());
    return new PageImpl<>(mentoringDtoList.subList(start, end),
        pageRequest, mentoringDtoList.size());
  }

  @Transactional
  public void mentoringFollow(String email, Long mentoringId){

    User likeUser = userService.getUser(email);

    Mentoring mentoring = getMentoring(mentoringId);
    List<User> followerList = mentoring.getFollowerList();
    if (followerList.stream().anyMatch(user -> user.getId().equals(likeUser.getId()))) {
      followerList.removeIf(user -> user.equals(likeUser));
    }
    else{
      followerList.add(likeUser);
    }
  }

  @Transactional
  public Page<Mentoring> getFollowMentoring(String email, Pageable pageable){
    User user = userService.getUser(email);
    return mentoringRepository.findByStatusNotAndFollowerList_Id(MentoringStatus.DELETE, user.getId(), pageable);
  }

  @Transactional
  public Page<Mentoring> getMentoringHistory(Long userId, Pageable pageable){
    User user = userService.getUserById(userId);
    return mentoringRepository.findByStatusNotAndUserId(MentoringStatus.DELETE, user.getId(), pageable);
  }

  @Transactional
  public Page<Mentoring> getParticipatedMentoring(String email, Pageable pageable){
    User user = userService.getUser(email);
    List<Mentoring> mentoringList = menteeService.getMentoringListFormMenteeUser(user)
        .stream()
        .filter(mentoring -> mentoring.getStatus().equals(MentoringStatus.PROGRESS))
        .collect(Collectors.toList());

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), mentoringList.size());

    List<Mentoring> sublist = new ArrayList<>();
    if (start <= end) {
      sublist = mentoringList.subList(start, end);
    }

    return new PageImpl<>(sublist, pageable, mentoringList.size());
  }

  @Transactional
  public void mentoringFinish(String email, Long mentoringId){
    User user = userService.getUser(email);
    Mentoring mentoring = getMentoring(mentoringId);

    if(!user.getId().equals(mentoring.getUser().getId())){
      throw new AppException(HttpStatus.BAD_REQUEST, "멘토링 등록자가 아닙니다.");
    }

    if(LocalDate.now().isBefore(mentoring.getEndDate())){
      throw new AppException(HttpStatus.BAD_REQUEST, "종료 신청은 멘토링 종료일에만 할 수 있습니다.");
    }

    mentoring.setStatus(MentoringStatus.FINISH);
    mentoringRepository.save(mentoring);
  }

  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void whenMentoringPeriodEndStatusChangesToFinish() {
    LocalDate now = LocalDate.now();
    List<Mentoring> mentoringList = mentoringRepository.findAllByEndDateIsBeforeAndStatusIs(
        now, MentoringStatus.PROGRESS);
    mentoringList.forEach(mentoring -> mentoring.setStatus(MentoringStatus.FINISH));
  }
}