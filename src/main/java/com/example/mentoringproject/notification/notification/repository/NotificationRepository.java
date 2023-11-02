package com.example.mentoringproject.notification.notification.repository;

import com.example.mentoringproject.notification.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByReceiverEmailAndIsReadOrderByRegisterDateDesc(String email, boolean read);
}
