package com.example.mentoringproject.profile.controller;

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
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ProfileDto profileDto
    ) {
        profileService.createProfile(token, profileDto);
        return ResponseEntity.ok("profile create success");
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ProfileDto profileDto
    ) {
        profileService.updateProfile(token, profileDto);
        return ResponseEntity.ok("profile update success");
    }

    @GetMapping
    public ResponseEntity<ProfileInfo>  getProfile(
            @RequestHeader(value = "Authorization") String token
    ) {
       return ResponseEntity.ok(ProfileInfo.from(profileService.getProfile(token)));
    }

    @DeleteMapping
    public ResponseEntity<?> DeleteProfile(
            @RequestHeader(value = "Authorization") String token
    ) {
        profileService.deleteProfile(token);
        return ResponseEntity.ok("profile delete success");
    }

}
