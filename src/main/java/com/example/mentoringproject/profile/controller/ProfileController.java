package com.example.mentoringproject.profile.controller;

import com.example.mentoringproject.common.util.SpringSecurityUtil;
import com.example.mentoringproject.profile.entity.Profile;
import com.example.mentoringproject.profile.model.ProfileDto;
import com.example.mentoringproject.profile.model.ProfileInfo;
import com.example.mentoringproject.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping
    public ResponseEntity<?> createProfile(
            @RequestBody ProfileDto profileDto
    ) {
        String email = SpringSecurityUtil.getLoginEmail();
        profileService.createProfile(email, profileDto);
        return ResponseEntity.ok("profile create success");
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestBody ProfileDto profileDto
    ) {
        String email = SpringSecurityUtil.getLoginEmail();
        profileService.updateProfile(email, profileDto);
        return ResponseEntity.ok("profile update success");
    }

    @GetMapping
    public ResponseEntity<ProfileInfo>  profileInfo() {
        String email = SpringSecurityUtil.getLoginEmail();
        return ResponseEntity.ok(profileService.profileInfo(email));
    }

    @DeleteMapping
    public ResponseEntity<?> DeleteProfile() {
        String email = SpringSecurityUtil.getLoginEmail();
        profileService.deleteProfile(email);
        return ResponseEntity.ok("profile delete success");
    }

}
