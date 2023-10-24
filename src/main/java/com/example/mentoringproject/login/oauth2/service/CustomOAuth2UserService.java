package com.example.mentoringproject.login.oauth2.service;

import com.example.mentoringproject.login.oauth2.CustomOAuth2User;
import com.example.mentoringproject.login.oauth2.OAuthAttributes;
import com.example.mentoringproject.user.entity.SocialType;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  private static final String NAVER = "naver";
  private static final String KAKAO = "kakao";

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    SocialType socialType = getSocialType(registrationId);
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    Map<String, Object> attributes = oAuth2User.getAttributes();
    OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

    User createdUser = getUser(extractAttributes, socialType);

    return new CustomOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("USER")),
        attributes,
        extractAttributes.getNameAttributeKey(),
        createdUser.getEmail()
    );
  }

  private SocialType getSocialType(String registrationId) {
    if(NAVER.equals(registrationId)) {
      return SocialType.NAVER;
    }
    if(KAKAO.equals(registrationId)) {
      return SocialType.KAKAO;
    }
    return SocialType.GOOGLE;
  }

  private User getUser(OAuthAttributes attributes, SocialType socialType) {
    //기존에 등록된 User인지 확인
    User findUser = userRepository.findBySocialTypeAndSocialId(socialType,
        attributes.getOauth2UserInfo().getId()).orElse(null);

    //기존 유저가 아니면 db에 User save
    if(findUser == null) {
      return saveUser(attributes, socialType);
    }
    return findUser;
  }

  private User saveUser(OAuthAttributes attributes, SocialType socialType) {
    User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
    return userRepository.save(createdUser);
  }
}