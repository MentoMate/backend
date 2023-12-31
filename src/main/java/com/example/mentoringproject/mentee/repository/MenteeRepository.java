package com.example.mentoringproject.mentee.repository;

import com.example.mentoringproject.mentee.entity.Mentee;
import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.user.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {

  List<Mentee> findAllByMentoring(Mentoring mentoring);

  List<Mentee> findAllByUser(User user);

  Page<Mentee> findAllByMentoringInAndRatingIsNotNull(List<Mentoring> mentoring, Pageable pageable);

}
