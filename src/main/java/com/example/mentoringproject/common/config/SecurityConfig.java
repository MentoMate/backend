package com.example.mentoringproject.common.config;

import com.example.mentoringproject.common.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.login.email.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.example.mentoringproject.login.email.handler.LoginFailureHandler;
import com.example.mentoringproject.login.email.handler.LoginSuccessHandler;
import com.example.mentoringproject.login.email.service.LoginService;
import com.example.mentoringproject.user.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginService loginService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;

  private static final String[] PERMIT_SWAGGER_URL_ARRAY = PermitUrl.PERMIT_SWAGGER_URL_ARRAY;
  private static final String[] PERMIT_GET_MENTORING_URL_ARRAY = PermitUrl.PERMIT_GET_MENTORING_URL_ARRAY;
  private static final String[] PERMIT_GET_POST_URL_ARRAY = PermitUrl.PERMIT_GET_POST_URL_ARRAY;
  private static final String[] PERMIT_GET_USER_URL_ARRAY = PermitUrl.PERMIT_GET_USER_URL_ARRAY;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable() // httpBasic 사용 X
        .csrf().disable() // csrf 보안 사용 X
        .cors().configurationSource(corsConfigurationSource())
        .and()
        .formLogin().disable() // FormLogin 사용 X
        .headers().frameOptions().disable()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/user/login/**", "/user/join/**").permitAll()
        .antMatchers(PERMIT_SWAGGER_URL_ARRAY).permitAll()
        .antMatchers().permitAll()
        .antMatchers(HttpMethod.GET, PERMIT_GET_MENTORING_URL_ARRAY).permitAll()
        .antMatchers(HttpMethod.GET, PERMIT_GET_POST_URL_ARRAY).permitAll()
        .antMatchers(HttpMethod.GET, PERMIT_GET_USER_URL_ARRAY).permitAll()
        .antMatchers("/ws/**").permitAll()
        .antMatchers("/ws/*").permitAll()
        .antMatchers("/ws/chat/*").permitAll()
        .antMatchers("/ws/chat/**").permitAll()
        .antMatchers("/subscribe/**").permitAll()
        .antMatchers("/topic/**").permitAll()
        .antMatchers("/chat/*").permitAll()
        .antMatchers("/chat/**").permitAll()

        .anyRequest().authenticated();

    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
    http.addFilterBefore(jwtAuthenticationProcessingFilter(),
        CustomJsonUsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.addAllowedOrigin("http://localhost:5173"); // 로컬
    config.addAllowedOrigin("http://localhost:3000"); // 로컬
    config.addAllowedMethod("*"); // 모든 메소드 허용.
    config.addAllowedHeader("*");
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }


  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(encoder());
    provider.setUserDetailsService(loginService);
    return new ProviderManager(provider);
  }

  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtService, userRepository);
  }


  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler();
  }

  @Bean
  public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
    CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
        = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper, encoder());
    customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
    customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return customJsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(
        jwtService, userRepository);
    return jwtAuthenticationFilter;
  }
}
