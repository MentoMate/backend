package com.example.mentoringproject.mentoring.schedule.file.model;

import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import java.time.LocalDateTime;
import javax.persistence.Column;
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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileUploadDto {
  private Long fileId;
  private String fileName;
  private String uploadUrl;
  private Long scheduleId;

}
