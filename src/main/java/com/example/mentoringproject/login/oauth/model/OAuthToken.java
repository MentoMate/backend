package com.example.mentoringproject.login.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OAuthToken {

  private String access_token;
  private int expires_in;
  private String scope;
  private String token_type;
  private String id_token;
}
