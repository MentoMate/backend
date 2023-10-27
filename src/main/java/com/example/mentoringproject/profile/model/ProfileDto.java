package com.example.mentoringproject.profile.model;


import com.example.mentoringproject.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ProfileDto {

    private String name;
    private int career;
    private String introduce;
    private String mainCategory;
    private String middleCategory;
    private String imgUrl;
}
