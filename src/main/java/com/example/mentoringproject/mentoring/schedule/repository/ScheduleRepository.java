package com.example.mentoringproject.mentoring.schedule.repository;

import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByMentoring_IdAndStartBetween(Long mentoringId, LocalDate startDate, LocalDate endDate);
}
