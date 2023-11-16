package com.example.mentoringproject.common.config;

public class PermitUrl {
  public static final String[] PERMIT_SWAGGER_URL_ARRAY = {
      "/v3/api-docs/**",
      "/swagger-ui/**"
  };
  public static final String[] PERMIT_GET_MENTORING_URL_ARRAY = {
      "/mentoring",
      "/mentoring/{mentoringId}",
      "/mentoring/main",
      "/mentoring/search",
      "/mentoring/{userId}/history",
      "/mentoring/mentor/review",
      "/mentor/search"
  };
  public static final String[] PERMIT_GET_POST_URL_ARRAY = {
      "/posts",
      "/posts/{postId}/comments",
      "/posts/{postId}/info",
      "/post/search","/{postId}/comments",
  };
  public static final String[] PERMIT_GET_USER_URL_ARRAY = {
      "/user/profile", "/user/profile/{userId}"
  };
}
