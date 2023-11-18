package com.example.mentoringproject.pay.repository;

import com.example.mentoringproject.mentoring.entity.Mentoring;
import com.example.mentoringproject.pay.entity.Pay;
import com.example.mentoringproject.user.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {

  Page<Pay> findAllByUserOrderByUpdateDate(User user, Pageable pageable);

  Pay findByMentoringAndUser(Mentoring mentoring, User user);
}
