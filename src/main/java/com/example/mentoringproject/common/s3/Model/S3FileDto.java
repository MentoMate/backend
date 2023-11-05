package com.example.mentoringproject.common.s3.Model;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.model.MentoringSave;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.model.UserProfile;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S3FileDto {
  private String uploadUrl;

}