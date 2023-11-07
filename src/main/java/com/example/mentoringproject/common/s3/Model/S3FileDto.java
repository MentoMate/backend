package com.example.mentoringproject.common.s3.Model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S3FileDto {
  private String uploadUrl;

}