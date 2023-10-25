package com.example.mentoringproject.profile.service;

import com.example.mentoringproject.profile.entity.Profile;
import com.example.mentoringproject.profile.model.ProfileDto;
import com.example.mentoringproject.profile.repository.ProfileRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    @Transactional
    public void createProfile(String token, ProfileDto profileDto) {
        User user = userService.getUser(token);

        if(profileExists(user.getId())){
            throw new RuntimeException("프로필이 등록 되어 있습니다.");
        }

        profileRepository.save(Profile.builder()
                        .name(profileDto.getName())
                        .career(profileDto.getCareer())
                        .introduce(profileDto.getIntroduce())
                        .mainCategory(profileDto.getMainCategory())
                        .middleCategory(profileDto.getMiddleCategory())
                        .imgUrl(profileDto.getImgUrl())
                        .user(user)
                        .registerDate(LocalDateTime.now())
                        .build());
    }

    @Transactional
    public void updateProfile(String token, ProfileDto profileDto) {
        User user = userService.getUser(token);

        if(!profileExists(user.getId())){
            throw new RuntimeException("프로필 정보가 등록 되어 있지 않습니다.");
        }

        Profile profile = profileRepository.findByUserId(user.getId()).get();
        profile.setName(profileDto.getName());
        profile.setCareer(profileDto.getCareer());
        profile.setIntroduce(profileDto.getIntroduce());
        profile.setMainCategory(profileDto.getMainCategory());
        profile.setMiddleCategory(profile.getMiddleCategory());
        profile.setImgUrl(profileDto.getImgUrl());
        profile.setUpdateDate(LocalDateTime.now());

        profileRepository.save(profile);
    }

    @Transactional
    public void deleteProfile(String token) {
        User user = userService.getUser(token);

        if(!profileExists(user.getId())){
            throw new RuntimeException("프로필 정보가 등록 되어 있지 않습니다.");
        }

        Profile profile = profileRepository.findByUserId(user.getId()).get();
        profileRepository.delete(profile);
    }

    public Profile getProfile(String token){
        User user = userService.getUser(token);

        if(!profileExists(user.getId())){
            throw new RuntimeException("프로필 정보가 등록 되어 있지 않습니다.");
        }

        return profileRepository.findByUserId(user.getId()).get();
    }

    public boolean profileExists(Long userId){
        return  profileRepository.findByUserId(userId).isPresent();
    }
}
