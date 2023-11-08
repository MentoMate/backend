package com.example.mentoringproject.mentoring.schedule.file.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.schedule.file.repository.FileUploadRepository;
import com.example.mentoringproject.mentoring.schedule.service.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class FileUploadService {
  private final FileUploadRepository fileUploadRepository;
  private final ScheduleService scheduleService;
  private final S3Service s3Service;

  public static final String FOLDER = "schedule/file";
  private static final String UPLOAD_FILETYPE = "img";
  public void fileUpload(Long scheduleId, List<MultipartFile> multipartFileList){

    if(multipartFileList == null) throw new AppException(HttpStatus.BAD_REQUEST, "파일이 없습니다.");

    Schedule schedule = scheduleService.scheduleInfo(scheduleId);

    //멘티 테이블 확인

    s3Service.upload(multipartFileList, FOLDER, UPLOAD_FILETYPE);

  }
}
