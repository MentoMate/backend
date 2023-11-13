package com.example.mentoringproject.mentoring.schedule.file.entity;

import com.example.mentoringproject.common.s3.Model.S3FileDto;
import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import com.example.mentoringproject.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "file_schedule")
@EntityListeners(AuditingEntityListener.class)
public class FileUpload {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long id;

  private String fileName;
  private String uploadFolder;
  private String uploadUrl;

  @ManyToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @CreatedDate
  private LocalDateTime registerDate;

  public static List<FileUpload> from(Schedule schedule, List<S3FileDto> s3FileDtoList, User user){
    return  s3FileDtoList.stream()
        .map(s3FileDto -> FileUpload.builder()
            .fileName(s3FileDto.getFileName())
            .uploadFolder(s3FileDto.getUploadFolder())
            .uploadUrl(s3FileDto.getUploadUrl())
            .schedule(schedule)
            .user(user)
            .build())
        .collect(Collectors.toList());
  }
}
