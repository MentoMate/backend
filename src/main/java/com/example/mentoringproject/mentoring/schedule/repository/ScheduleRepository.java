package com.example.mentoringproject.mentoring.schedule.repository;

import com.example.mentoringproject.mentoring.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
