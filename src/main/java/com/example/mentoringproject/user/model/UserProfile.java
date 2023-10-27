package com.example.mentoringproject.user.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserProfile {

    private String name;
    private int career;
    private String introduce;
    private String mainCategory;
    private String middleCategory;
    private String imgUrl;
}
