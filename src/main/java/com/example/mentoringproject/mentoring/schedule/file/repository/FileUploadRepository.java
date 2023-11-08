package com.example.mentoringproject.mentoring.schedule.file.repository;

import com.example.mentoringproject.mentoring.schedule.file.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
