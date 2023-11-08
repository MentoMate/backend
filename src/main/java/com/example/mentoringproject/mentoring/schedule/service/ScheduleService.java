package com.example.mentoringproject.mentoring.schedule.service;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.common.s3.Service.S3Service;
import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleInfo;
import com.example.mentoringproject.mentoring.schedule.model.ScheduleSave;
import com.example.mentoringproject.mentoring.schedule.repository.ScheduleRepository;
import com.example.mentoringproject.mentoring.service.MentoringService;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.service.UserService;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final MentoringService mentoringService;
  private final UserService userService;
  private final S3Service s3Service;

  public static final String FOLDER = "schedule";
  public Schedule createSchedule(String email, ScheduleSave scheduleSave){
    Mentoring mentoring = mentoringService.getMentoring(scheduleSave.getMentoringId());

    scheduleRegisterAuth(email, mentoring);

    s3Service.fileClear(FOLDER + "/" + scheduleSave.getUploadFolder(), imgUrlChange(scheduleSave));

    return scheduleRepository.save(Schedule.from(scheduleSave, mentoring));
  }
  public Schedule updateSchedule(String email, ScheduleSave scheduleSave){
    Mentoring mentoring = mentoringService.getMentoring(scheduleSave.getMentoringId());

    scheduleRegisterAuth(email,mentoring);
    Schedule schedule = scheduleChk(scheduleSave.getScheduleId());

    schedule.setTitle(scheduleSave.getTitle());
    schedule.setContent(scheduleSave.getContent());
    schedule.setStartDate(scheduleSave.getStartDate());

    s3Service.fileClear(FOLDER + "/" + scheduleSave.getUploadFolder(), imgUrlChange(scheduleSave));

    return scheduleRepository.save(schedule);
  }

  public void deleteSchedule(String email,  Long scheduleId){
    Schedule schedule = scheduleChk(scheduleId);

    Mentoring mentoring = mentoringService.getMentoring(schedule.getMentoring().getId());
    scheduleRegisterAuth(email, mentoring);

    scheduleRepository.delete(schedule);
  }

  public Schedule scheduleInfo(Long scheduleId){
    return  scheduleChk(scheduleId);
  }

  public List<Schedule> scheduleInfoByPeriod(Long mentoringId, LocalDate startDate, LocalDate endDate){
      return  scheduleRepository.findByMentoring_IdAndStartDateBetween(mentoringId, startDate, endDate);
  }

  private void scheduleRegisterAuth(String email, Mentoring mentoring){
    User user = userService.getUser(email);

    if(user.getId() != mentoring.getUser().getId()){
      throw new AppException(HttpStatus.BAD_REQUEST, "일정은 멘토만 추가 및 삭제 할 수 있습니다.");
    }
  }

  private Schedule scheduleChk(Long scheduleId){
    return scheduleRepository.findById(scheduleId).orElseThrow(
        () -> new AppException(HttpStatus.BAD_REQUEST,"존재하지 않는 일정입니다."));
  }

  private List<String> imgUrlChange(ScheduleSave scheduleSave){
    return Optional.ofNullable(scheduleSave.getUploadImg())
        .orElse(Collections.emptyList())
        .stream()
        .map(s3Service::extractFileName)
        .collect(Collectors.toList());
  }
}
