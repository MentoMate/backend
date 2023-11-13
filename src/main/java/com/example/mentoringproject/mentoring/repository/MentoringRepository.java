package com.example.mentoringproject.mentoring.repository;

import com.example.mentoringproject.mentoring.entity.Mentoring;

import com.example.mentoringproject.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
  @Modifying
  @Query("update mentoring set countWatch = countWatch + 1 where id = :id")
  void updateCount(@Param("id") Long id);


  Page<Mentoring> findByStatusNotAndFollowerList_Id (MentoringStatus status, Long userId, Pageable pageable);
  Page<Mentoring> findByStatusNotAndUserId (MentoringStatus status, Long userId, Pageable pageable);
  Page<Mentoring> findByStatusNotAndMenteeList_Id (MentoringStatus status, Long userId, Pageable pageable);
  List<Mentoring> findTop50ByOrderByCountWatchDesc();

  List<Mentoring> findByEndDateBetween(LocalDate today, LocalDate maxEndDate);

}
