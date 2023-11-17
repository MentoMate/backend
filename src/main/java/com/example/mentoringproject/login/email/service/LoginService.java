package com.example.mentoringproject.login.email.service;

import com.example.mentoringproject.login.email.user.EmailLoginUser;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public EmailLoginUser loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
    return EmailLoginUser.builder()
        .username(user.getEmail())
        .userId(user.getId())
        .nickname(user.getNickName())
        .password(user.getPassword())
        .authorities(getGrantedAuthorities())
        .build();
  }

  @NotNull
  private static Collection<GrantedAuthority> getGrantedAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    return authorities;
  }

}