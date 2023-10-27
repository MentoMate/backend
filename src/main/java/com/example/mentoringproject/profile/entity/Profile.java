package com.example.mentoringproject.profile.entity;

import com.example.mentoringproject.profile.model.ProfileDto;
import com.example.mentoringproject.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    private String name;

    private int career;
    private String introduce;
    private String mainCategory;
    private String middleCategory;
    private String imgUrl;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private LocalDateTime registerDate;
    private LocalDateTime updateDate;

    public static Profile from(User user, ProfileDto profileDto){
       return Profile.builder()
            .name(profileDto.getName())
            .career(profileDto.getCareer())
            .introduce(profileDto.getIntroduce())
            .mainCategory(profileDto.getMainCategory())
            .middleCategory(profileDto.getMiddleCategory())
            .imgUrl(profileDto.getImgUrl())
            .user(user)
            .registerDate(LocalDateTime.now())
            .build();
    };
}
