package com.example.mentoringproject.mentoring.schedule.file.model;

import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.mentoring.schedule.file.entity.FileUpload;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileUploadInfo {
  private Long fileId;
  private Long scheduleId;
  private String fileName;
  private String uploadUrl;

  public static List<FileUploadInfo> from(List<FileUpload> fileUploadList){
    return  fileUploadList.stream()
        .map(fileUpload -> FileUploadInfo.builder()
            .fileId(fileUpload.getId())
            .scheduleId(fileUpload.getSchedule().getId())
            .fileName(fileUpload.getFileName())
            .uploadUrl(fileUpload.getUploadUrl())
            .build())
        .collect(Collectors.toList());
  }
}
