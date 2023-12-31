package com.example.mentoringproject.mentoring.file.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.model.S3FileDto;
import com.example.mentoringproject.common.s3.service.S3Service;
import com.example.mentoringproject.mentee.service.MenteeService;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.file.entity.FileUpload;
import com.example.mentoringproject.mentoring.file.repository.FileUploadRepository;
import com.example.mentoringproject.mentoring.schedule.repository.ScheduleRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
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
  private final ScheduleRepository scheduleRepository;
  private final UserService userService;
  private final S3Service s3Service;
  private final MenteeService menteeService;

  public static final String FOLDER = "schedule/file";
  private static final String UPLOAD_FILETYPE = "file";

  public List<FileUpload> fileUpload(String email, Long scheduleId, List<MultipartFile> multipartFileList){

    if(multipartFileList == null) throw new AppException(HttpStatus.BAD_REQUEST, "파일이 없습니다.");

    User user = userService.getUser(email);
    Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
            ()-> new AppException(HttpStatus.BAD_REQUEST, "존재하지 않는 일정입니다."));

    List<User> menteeList = menteeService.getUserListFormMentoring(schedule.getMentoring());
    if (menteeList.stream().noneMatch(mentee -> mentee.getId().equals(user.getId())) && !(user.getId().equals(schedule.getMentoring().getUser().getId())) ) {
      throw new AppException(HttpStatus.BAD_REQUEST, "멘토링 참가자가 아닙니다.");
    }

    List<S3FileDto> s3FileDtoList = s3Service.upload(multipartFileList, FOLDER, UPLOAD_FILETYPE);

    return fileUploadRepository.saveAll(FileUpload.from(schedule, s3FileDtoList, user));
  }

  public void fileDelete(String email, Long fileId){
    User user = userService.getUser(email);
    FileUpload fileUpload = fileUploadRepository.findById(fileId)
        .orElseThrow(()-> new AppException(HttpStatus.BAD_REQUEST, "존재하지 않는 파일입니다."));

    if(!fileUpload.getUser().getId().equals(user.getId())){
      throw new AppException(HttpStatus.BAD_REQUEST, "업로드 작성자만 삭제할 수 있습니다.");
    }

    s3Service.deleteFile(S3FileDto.from(fileUpload));
    fileUploadRepository.deleteById(fileId);
  }

  public List<FileUpload> fileUploadList(Long ScheduleId){
    List<FileUpload>  fileUploadList = fileUploadRepository.findByScheduleId(ScheduleId);
    return  fileUploadList;
  }

  public void fileListDelete(Long scheduleId){
    List<FileUpload>  fileUploadList = fileUploadRepository.findByScheduleId(scheduleId);
    fileUploadRepository.deleteAllInBatch(fileUploadList);
    s3Service.deleteFile(S3FileDto.from(fileUploadList));
  }

}
