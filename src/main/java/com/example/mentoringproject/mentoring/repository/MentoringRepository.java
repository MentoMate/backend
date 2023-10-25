package com.example.mentoringproject.mentoring.repository;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoringRepository extends JpaRepository<Mentoring, Long> {

}
