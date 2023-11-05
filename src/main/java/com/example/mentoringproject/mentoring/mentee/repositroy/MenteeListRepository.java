package com.example.mentoringproject.mentoring.mentee.repositroy;

import com.example.mentoringproject.mentoring.mentee.entity.MenteeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenteeListRepository extends JpaRepository<MenteeList, Long> {

}
