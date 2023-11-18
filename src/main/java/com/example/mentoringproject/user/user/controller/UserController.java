package com.example.mentoringproject.user.user.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.user.user.model.UserInfoDto;
import com.example.mentoringproject.user.user.model.UserJoinDto;
import com.example.mentoringproject.user.user.model.UserProfile;
import com.example.mentoringproject.user.user.model.UserProfileList;
import com.example.mentoringproject.user.user.model.UserProfileSave;
import com.example.mentoringproject.user.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Multipart;


@Tag(name = "User", description = "유저 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Operation(summary = "이메일 코드번호 전송", description = "이메일 코드번호 전송을 위해 이메일사용", responses = {
      @ApiResponse(responseCode = "200", description = "이메일 코드 전송 성공")
  })
  @PostMapping("/join/email/auth")
  public ResponseEntity<Boolean> sendEmailAuth(@RequestParam("email")
  @Email String email) {

    userService.verifyExistEmail(email);
    userService.sendEmailAuth(email);

    return ResponseEntity.ok(true);
  }

  @Operation(summary = "이메일 인증 코드 입력", description = "이메일과 인증코드를 이용해 이메일 확인", responses = {
      @ApiResponse(responseCode = "200", description = "이메일 인증 확인")
  })

  @PostMapping("/join/email/auth/verify")
  public ResponseEntity<Boolean> verifyEmailAuth(@RequestParam("email")
  @Email @NotBlank(message = "email은 필수값입니다.") String email,
      @RequestParam("authCode") @NotBlank(message = "authCode는 필수값입니다.") String authCode) {
    return ResponseEntity.ok(userService.verifyEmailAuth(email, authCode));
  }

  @Operation(summary = "닉네임 유무 확인", description = "닉네임 유무 확인", responses = {
      @ApiResponse(responseCode = "200", description = "이메일 인증 확인")
  })
  @PostMapping("/join/email/nickname/verify")
  public ResponseEntity<Boolean> checkDuplicateNickname(@RequestParam ("nickName")
  @Length(min = 2, max = 10, message = "nickName은 최소 2자 최대 20자 입니다.") String nickName) {
    return ResponseEntity.ok(userService.checkDuplicateNickName(nickName));
  }

  @Operation(summary = "회원가입", description = "닉네임, 이메일, 비밀번호로 회원가입", responses = {
      @ApiResponse(responseCode = "200", description = "회원가입 완료")
  })
  @PostMapping("/join/email")
  public ResponseEntity<String> joinEmailUser(@RequestBody @Valid UserJoinDto parameter) {
    userService.joinEmailUser(parameter);
    return ResponseEntity.ok("email join success");
  }
  @Operation(summary = "유저 정보 가져오기 api", description = "이름 이메일, 닉네임 가져오기", responses = {
      @ApiResponse(responseCode = "200", description = "유저 정보 가져오기 완료", content = @Content(schema =
      @Schema(implementation = UserInfoDto.class)))
  })
  @GetMapping("/info")
  public ResponseEntity<UserInfoDto> getUserInfo() {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(userService.getUserInfo(email));
  }

  @Operation(summary = "닉네임 변경 api", description = "닉네임 변경 api", responses = {
      @ApiResponse(responseCode = "200", description = "닉네임 변경 완료", content = @Content(schema =
      @Schema(implementation = UserInfoDto.class)))
  })
  @PutMapping("/nickname/change")
  public ResponseEntity<UserInfoDto> changeNickname(@RequestParam String nickname) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(userService.changeNickname(email, nickname));
  }

  @Operation(summary = "사진 변경 api", description = "사진 변경 api", responses = {
      @ApiResponse(responseCode = "200", description = "사진 변경 완료", content = @Content(schema =
      @Schema(implementation = UserInfoDto.class)))
  })
  @PutMapping("/img")
  public ResponseEntity<UserInfoDto> changeImg(
      @RequestPart(name = "img") List<MultipartFile> multipartFile) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(userService.changeImg(email, multipartFile));
  }


  @Operation(summary = "프로필 등록 api", description = "프로필 등록 api", responses = {
      @ApiResponse(responseCode = "200", description = "프로필 등록 성공", content =
      @Content(schema = @Schema(implementation = UserProfile.class)))
  })
  @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserProfile> createProfile(
      @RequestPart UserProfileSave userProfileSave,
      @RequestPart(name = "img") List<MultipartFile> multipartFile

  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(
        UserProfile.from(userService.createProfile(email, userProfileSave, multipartFile)));
  }

  @Operation(summary = "프로필 수정 api", description = "프로필 수정 api", responses = {
      @ApiResponse(responseCode = "200", description = "프로필 수정 성공", content =
      @Content(schema = @Schema(implementation = UserProfile.class)))
  })
  @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserProfile> updateProfile(
      @RequestPart UserProfileSave userProfileSave,
      @RequestPart(name = "img") List<MultipartFile> multipartFile
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(
        UserProfile.from(userService.updateProfile(email, userProfileSave, multipartFile)));
  }

  @Operation(summary = "프로필 조회 api", description = "프로필 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "프로필 조회 성공", content =
      @Content(schema = @Schema(implementation = UserProfile.class)))
  })
  @GetMapping("/profile/{userId}")
  public ResponseEntity<UserProfile> profileInfo(
      @PathVariable Long userId
  ) {
    return ResponseEntity.ok(UserProfile.from(userService.profileInfo(userId)));
  }

  @Operation(summary = "프로필 목록 조회 api", description = "프로필 목록 조회 api", responses = {
      @ApiResponse(responseCode = "200", description = "프로필 목록 조회 성공", content =
      @Content(schema = @Schema(implementation = UserProfileList.class)))
  })

  @GetMapping("/profile")
  public ResponseEntity<Page<UserProfileList>> getProfileList(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);

    return ResponseEntity.ok(UserProfileList.from(userService.getProfileList(pageable)));
  }

  @PostMapping("/{userId}")
  public ResponseEntity<Void> userFollow(
      @PathVariable Long userId
  ) {
    String email = SpringSecurityUtil.getLoginEmail();
    userService.userFollow(email, userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/profile/follow")
  public ResponseEntity<Page<UserProfileList>> getFollowProfileList(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int pageSize,
      @RequestParam(defaultValue = "id") String sortId,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page - 1, pageSize, direction, sortId);
    String email = SpringSecurityUtil.getLoginEmail();
    return ResponseEntity.ok(UserProfileList.from(userService.getFollowProfileList(email,pageable)));
  }
}