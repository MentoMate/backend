package com.example.mentoringproject.user.entity;

public enum SocialType {
  NAVER, KAKAO, GOOGLE;

  public static SocialType getSocialType(String registrationId) {
    if ("NAVER".equals(registrationId)) {
      return SocialType.NAVER;
    }
    if ("KAKAO".equals(registrationId)) {
      return SocialType.KAKAO;
    }
    return SocialType.GOOGLE;
  }
}
