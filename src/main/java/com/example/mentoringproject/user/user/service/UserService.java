package com.example.mentoringproject.user.user.service;

import com.example.mentoringproject.ElasticSearch.mentor.entity.MentorSearchDocumment;
import com.example.mentoringproject.ElasticSearch.mentor.repository.MentorSearchRepository;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.login.email.components.MailComponents;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.model.UserInfoDto;
import com.example.mentoringproject.user.user.model.UserJoinDto;
import com.example.mentoringproject.user.user.model.UserProfileSave;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;
  private final MailComponents mailComponents;
  private final MentorSearchRepository mentorSearchRepository;
  private final S3Service s3Service;

  private static final String FOLDER = "profile/";
  private static final String FILE_TYPE = "img";
  //인증 확인 이메일을 보내고 DB에 저장

  @Transactional
  public boolean verifyExistEmail(String email) {

    //이메일이 존재하지 않으면 이메일 인증 가능
    if (!userRepository.existsByEmail(email)) {
      return true;
    }

    //만약 이미 이메일이 존재하면 회원가입이 완료된 이메일인지 확인
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "server error"));

    //회원가입이 완료된 이메일이면 exception
    if (user.getRegisterDate() != null) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.");
    }

    //회원가입이 완료되지 않은 이메일이면 삭제해야됨
    userRepository.deleteByEmail(email);

    return true;
  }


  @Transactional
  public void sendEmailAuth(String email) {
    String authCode = String.valueOf((int) (Math.random() * 899999) + 100000);
    sendEmailAuth(email, authCode);
    userRepository.save(
        User.builder()
            .email(email)
            .emailAuth(authCode)
            .build()
    );
  }

  //인증을 위한 이메일 보내기
  private void sendEmailAuth(String email, String authCode) {
    String subject = "MentoringProject에 가입을 축하드립니다.";
    String text = "<p>MentoringProject에 가입을 축하드립니다.</p><p>인증 코드 : " + authCode + "</p>";
    mailComponents.sendMail(email, subject, text);
  }


  //이메일 인증 확인
  @Transactional
  public Boolean verifyEmailAuth(String email, String authCode) {
    User user = userRepository.findByEmailAndEmailAuth(email, authCode).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST, "이메일과 인증 코드가 일치하지 않습니다.")
    );
    user.setEmailAuthDate(LocalDateTime.now());
    return true;
  }

  public Boolean checkDuplicateNickName(String nickName) {
    Optional<User> user = userRepository.findByNickNameAndRegisterDateIsNotNull(nickName);
    if (user.isPresent()) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
    }
    return true;
  }


  @Transactional
  public void joinEmailUser(UserJoinDto parameter) {
    User user = userRepository.findByEmail(parameter.getEmail())
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다."));

    if (user.getEmailAuthDate()==null) {
      throw new AppException(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다.");
    }

    checkDuplicateNickName(parameter.getNickName());

    user.setNickName(parameter.getNickName());
    user.setPassword(encoder.encode(parameter.getPassword()));
    user.setRegisterDate(LocalDateTime.now());
    user.setRating(0.0);

  }

  @Transactional
  public User createProfile(String email, UserProfileSave userProfileSave,
      List<MultipartFile> multipartFile) {
    User user = getUser(email);

    if (userRepository.existsByIdAndNameIsNotNull(user.getId())) {
      throw new AppException(HttpStatus.BAD_REQUEST, "프로필이 등록 되어 있습니다.");
    }

    setProfile(user, userProfileSave);
    ImgUpload(multipartFile, user, userProfileSave);
    mentorSearchRepository.save(MentorSearchDocumment.fromEntity(user));

    return userRepository.save(user);

  }

  @Transactional
  public User updateProfile(String email, UserProfileSave userProfileSave,
      List<MultipartFile> multipartFile) {
    User user = getUser(email);

    if (!userRepository.existsByIdAndNameIsNotNull(user.getId())) {
      throw new AppException(HttpStatus.BAD_REQUEST, "프로필이 등록 되어 있지 않습니다.");
    }

    setProfile(user, userProfileSave);
    ImgUpload(multipartFile, user, userProfileSave);
    mentorSearchRepository.save(MentorSearchDocumment.fromEntity(user));

    return userRepository.save(user);
  }

  public User profileInfo(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."));

    if (!userRepository.existsByIdAndNameIsNotNull(user.getId())) {
      throw new AppException(HttpStatus.BAD_REQUEST, "프로필이 등록 되어 있지 않습니다.");
    }

    return user;
  }

  @Transactional(readOnly = true)
  public Page<User> getProfileList(Pageable pageable) {
    return userRepository.findByNameIsNotNull(pageable);
  }

  private User setProfile(User user, UserProfileSave userProfileSave) {

    user.setName(userProfileSave.getName());
    user.setCareer(userProfileSave.getCareer());
    user.setIntroduce(userProfileSave.getIntroduce());
    user.setMainCategory(userProfileSave.getMainCategory());
    user.setMiddleCategory(userProfileSave.getMiddleCategory());
    user.setUploadFolder(userProfileSave.getUploadFolder());

    return user;
  }

  public User getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."));
  }

  @Transactional
  public UserInfoDto changeImg(String email, List<MultipartFile> multipartFile) {

    User user = getUser(email);

    if(userRepository.existsByIdAndNameIsNotNull(user.getId())){
      s3Service.deleteFile(S3FileDto.from(user));
    }

    String uploadPath = FOLDER + user.getUploadFolder();
    List<S3FileDto> s3FileDto = s3Service.upload(multipartFile,uploadPath,FILE_TYPE);
    user.setUploadUrl(s3FileDto.get(0).getUploadUrl());
    userRepository.save(user);

    return UserInfoDto.from(user);

  }
  private void ImgUpload(List<MultipartFile> multipartFile, User user, UserProfileSave userProfile) {
    String uploadPath = FOLDER + user.getUploadFolder();
    List<S3FileDto> s3FileDto = s3Service.upload(multipartFile,uploadPath,FILE_TYPE);
    user.setUploadUrl(s3FileDto.get(0).getUploadUrl());

    List<String> imgList = Optional.ofNullable(userProfile.getUploadImg())
        .orElse(Collections.emptyList())
        .stream()
        .map(s3Service::extractFileName)
        .collect(Collectors.toList());
    imgList.add(s3Service.extractFileName(user.getUploadUrl()));

    s3Service.fileClear(uploadPath, imgList);
  }

  @Transactional
  public void userFollow(String email, Long userId){

    User followUser = getUser(email);
    User mentor = profileInfo(userId);
    List<User> followerList = mentor.getFollowerList();
    if (followerList.stream().anyMatch(user -> user.getId().equals(followUser.getId()))) {
      followerList.removeIf(user -> user.equals(followUser));
    }
    else{
      followerList.add(followUser);
    }
  }

  @Transactional(readOnly = true)
  public Page<User> getFollowProfileList(String email, Pageable pageable) {
    User user = getUser(email);
    return userRepository.findByNameIsNotNull(pageable);
  }

  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."));
  }

  public UserInfoDto getUserInfo(String email) {
    User user = getUser(email);
    return UserInfoDto.from(user);
  }

  @Transactional
  public UserInfoDto changeNickname(String email, String nickname) {
    User user = getUser(email);
    checkDuplicateNickName(nickname);
    user.setNickName(nickname);
    return UserInfoDto.from(user);
  }

  public boolean userMentorChk(String email){
    User user = getUser(email);
    return userRepository.existsByIdAndNameIsNotNull(user.getId());
  }

}