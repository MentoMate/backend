package com.example.mentoringproject.notification.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.notification.notification.emitter.repository.EmitterRepository;
import com.example.mentoringproject.notification.notification.entity.Notification;
import com.example.mentoringproject.notification.notification.model.NotificationDto;
import com.example.mentoringproject.notification.notification.repository.NotificationRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  @Mock
  private EmitterRepository emitterRepository;

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private NotificationService notificationService;


  @Test
  @DisplayName("공지 읽기 실패 - 잘못된 NotificationId")
  void readNotificationTestFail_WrongNotificationId() {
    //given

    when(notificationRepository.findById(anyLong()))
        .thenReturn(Optional.empty());
    //when
    AppException exception = assertThrows(AppException.class,
        () -> notificationService.readNotification(1L));
    //then
    assertEquals(exception.getErrorCode(), HttpStatus.BAD_REQUEST);
    assertEquals(exception.getMessage(), "Notification Not found");
  }

  @Test
  @DisplayName("공지 읽기 성공 - isRead가 true로 바뀌는지 확인")
  void readNotificationTestSuccess() {
    //given
    Notification notification = Notification.builder()
        .id(1L)
        .isRead(false)
        .build();
    when(notificationRepository.findById(anyLong()))
        .thenReturn(Optional.of(notification));
    //when
    NotificationDto notificationDto = notificationService.readNotification(1L);
    //then

    assertEquals(notificationDto.getIsRead(), true);
  }

  @Test
  @DisplayName("공지 삭제 실패 - 존재하지 않는 공지")
  void deleteNotificationTestFail_NoExistNotification() {
    //given
    when(notificationRepository.existsById(anyLong()))
        .thenReturn(false);
    //when
    AppException exception = assertThrows(AppException.class,
        () -> notificationService.deleteNotification(1L));
    //then
    assertEquals(exception.getErrorCode(), HttpStatus.BAD_REQUEST);
    assertEquals(exception.getMessage(), "존재하지 않는 공지 아이디입니다");
  }

  @Test
  @DisplayName("공지 삭제 성공-repository에서 deleteById메서드 실행 확인")
  void deleteNotificationTestSuccess() {
    //given
    Long notificationId = 1L;
    when(notificationRepository.existsById(anyLong()))
        .thenReturn(true);
    //when
    notificationService.deleteNotification(notificationId);
    //then
    verify(notificationRepository, times(1)).deleteById(notificationId);
  }

  @Test
  @DisplayName("읽지 않은 알림목록 가져오기 성공")
  void getUnreadNotificationTestSuccess() {
    //given
    User user = User.builder()
        .id(1L)
        .build();
    List<Notification> notificationList = new ArrayList<>();
    notificationList.add(Notification.builder()
        .id(1L)
        .receiver(user)
        .isRead(false)
        .build());
    notificationList.add(Notification.builder()
        .id(2L)
        .receiver(user)
        .isRead(false)
        .build());

    when(userService.getUser(anyString())).thenReturn(user);
    when(notificationRepository.findAllByReceiverAndIsReadIsFalseOrderByRegisterDateDesc(any(User.class)))
        .thenReturn(notificationList);
    //when
    List<NotificationDto> notificationDtoList = notificationService.getUnreadNotification("email");
    //then
    assertEquals(notificationDtoList.size(), 2);
    assertEquals(notificationDtoList.get(0).getNotificationId(), 1L);
  }
}