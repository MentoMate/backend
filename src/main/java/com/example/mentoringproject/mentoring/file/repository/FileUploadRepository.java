package com.example.mentoringproject.mentoring.file.repository;

import com.example.mentoringproject.mentoring.file.entity.FileUpload;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
  List<FileUpload> findByScheduleId (Long scheduleId);
}
