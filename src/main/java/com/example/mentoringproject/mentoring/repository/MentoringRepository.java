package com.example.mentoringproject.mentoring.repository;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
  @Modifying
  @Query("update mentoring set countWatch = countWatch + 1 where id = :id")
  void updateCount(Long id);
}
