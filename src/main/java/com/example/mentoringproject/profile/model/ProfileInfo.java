package com.example.mentoringproject.profile.model;

import com.example.mentoringproject.profile.entity.Profile;
import com.example.mentoringproject.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileInfo {
    private Long profileId;
    private String name;
    private int career;
    private String introduce;
    private String mainCategory;
    private String middleCategory;
    private String imgUrl;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    public static ProfileInfo from(Profile profile){
        return ProfileInfo.builder()
                .profileId(profile.getId())
                .name(profile.getName())
                .career(profile.getCareer())
                .introduce(profile.getIntroduce())
                .mainCategory(profile.getMainCategory())
                .middleCategory(profile.getMiddleCategory())
                .imgUrl(profile.getImgUrl())
                .registerDate(profile.getRegisterDate())
                .updateDate(profile.getUpdateDate())
                .build();
    }
}
