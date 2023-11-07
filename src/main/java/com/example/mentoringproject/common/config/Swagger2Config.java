package com.example.mentoringproject.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    name = "Authorization",
    description = "Auth Token"
)
@Configuration
public class Swagger2Config {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("api")
        .addOpenApiCustomiser(openApi -> {
          openApi.getInfo()
              .title("Mentoring Project")
              .description("Mentoring Project API Document")
              .version("1.0");
          openApi.addSecurityItem(new SecurityRequirement().addList("Authorization"));
        })
        .pathsToMatch("/**")
        .build();
  }

}