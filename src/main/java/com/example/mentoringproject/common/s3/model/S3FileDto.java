package com.example.mentoringproject.common.s3.model;

import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.mentoring.file.entity.FileUpload;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S3FileDto {
  private String fileName;
  private String uploadFolder;
  private String uploadUrl;

  public static List<S3FileDto> from(FileUpload fileUpload){
    List<S3FileDto> s3FileDtoList = new ArrayList<>();
    s3FileDtoList.add(S3FileDto.builder()
        .fileName(fileUpload.getFileName())
        .uploadFolder(fileUpload.getUploadFolder())
        .uploadUrl(fileUpload.getUploadUrl()).build());
    return  s3FileDtoList;
  }

  public static List<S3FileDto> from(List<FileUpload> fileUploadList){
    return  fileUploadList.stream()
        .map(s3FileDto -> S3FileDto.builder()
            .fileName(s3FileDto.getFileName())
            .uploadFolder(s3FileDto.getUploadFolder())
            .uploadUrl(s3FileDto.getUploadUrl())
            .build())
        .collect(Collectors.toList());
  }

  public static List<S3FileDto> from(User user){
    List<S3FileDto> s3FileDtoList = new ArrayList<>();
    s3FileDtoList.add(S3FileDto.builder()
        .uploadFolder("profile/" + user.getUploadFolder() + "/")
        .uploadUrl( user.getUploadUrl()).build());
    return  s3FileDtoList;
  }
}