package com.example.mentoringproject.common.s3.Model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S3FileDto {
  private String uploadPath;
  private String uploadName;
  private String uploadUrl;
}
