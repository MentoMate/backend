package com.example.mentoringproject.profile.service;

import com.example.mentoringproject.profile.entity.Profile;
import com.example.mentoringproject.profile.model.ProfileDto;
import com.example.mentoringproject.profile.model.ProfileInfo;
import com.example.mentoringproject.profile.repository.ProfileRepository;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void createProfile(String email, ProfileDto profileDto) {
        User user = getUser(email);

        if(profileExists(user.getId())){
            throw new RuntimeException("프로필이 등록 되어 있습니다.");
        }

        profileRepository.save( Profile.from(user, profileDto));
    }

    @Transactional
    public void updateProfile(String email, ProfileDto profileDto) {
        User user = getUser(email);

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
    public void deleteProfile(String email) {
        User user = getUser(email);

        if(!profileExists(user.getId())){
            throw new RuntimeException("프로필 정보가 등록 되어 있지 않습니다.");
        }

        Profile profile = profileRepository.findByUserId(user.getId()).get();
        profileRepository.delete(profile);
    }

    public ProfileInfo profileInfo(String email){
        User user = getUser(email);

        if(!profileExists(user.getId())){
            throw new RuntimeException("프로필 정보가 등록 되어 있지 않습니다.");
        }

        return ProfileInfo.from(profileRepository.findByUserId(user.getId()).get());
    }

    public boolean profileExists(Long userId){
        return  profileRepository.findByUserId(userId).isPresent();
    }

    private User getUser(String email){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일 입니다."));

        return user;
    }
}
