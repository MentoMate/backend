package com.example.mentoringproject.chat.service;

import com.example.mentoringproject.chat.entity.GroupChatRoom;
import com.example.mentoringproject.chat.entity.GroupMessage;
import com.example.mentoringproject.chat.entity.PrivateChatRoom;
import com.example.mentoringproject.chat.entity.PrivateMessage;
import com.example.mentoringproject.chat.model.GroupChatMessage;
import com.example.mentoringproject.chat.model.GroupChatRoomCreateResponse;
import com.example.mentoringproject.chat.model.PrivateChatMessage;
import com.example.mentoringproject.chat.model.PrivateChatRoomCreateRequest;
import com.example.mentoringproject.chat.repository.GroupChatRoomRepository;
import com.example.mentoringproject.chat.repository.GroupMessageRepository;
import com.example.mentoringproject.chat.repository.PrivateChatRoomRepository;
import com.example.mentoringproject.chat.repository.PrivateMessageRepository;
import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.chat.exception.ChatRoomAlreadyExistsException;
import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import com.example.mentoringproject.mentoring.mentoring.entity.MentoringStatus;
import com.example.mentoringproject.mentoring.mentoring.repository.MentoringRepository;
import com.example.mentoringproject.user.user.entity.User;
import com.example.mentoringproject.user.user.repository.UserRepository;
import com.example.mentoringproject.user.user.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

  private Map<String, GroupChatRoom> chatRooms;
  private final GroupMessageRepository groupMessageRepository;
  private final GroupChatRoomRepository groupChatRoomRepository;
  private final MentoringRepository mentoringRepository;
  private final UserRepository userRepository;
  private final PrivateChatRoomRepository privateChatRoomRepository;
  private final PrivateMessageRepository privateMessageRepository;
  private final UserService userService;

  @PostConstruct
  //의존관게 주입완료되면 실행되는 코드
  private void init() {
    chatRooms = new LinkedHashMap<>();
  }

  //  그룹 채팅방 생성 - (1)
  public void createRoomAutomatically() {
    List<Mentoring> mentorings = mentoringRepository
        .findAllByStartDateIsAndStatusIsAndRoomExistIsFalse(
            LocalDate.now(), MentoringStatus.PROGRESS
        );   for (Mentoring mentoring : mentorings) {
      createGroupRoom(mentoring);
    }
  }

  //  그룹 채팅방 생성 - (2)
  public Optional<GroupChatRoomCreateResponse> createGroupRoom(Mentoring mentoring) {
    LocalDate autoCreateChatRoomDate = mentoring.getStartDate();
    LocalDate currentDate = LocalDate.now();

    if (currentDate.isEqual(autoCreateChatRoomDate) || currentDate.isAfter(
        autoCreateChatRoomDate)) {
      GroupChatRoom groupChatRoom = new GroupChatRoom();
      groupChatRoom.setMentoring(mentoring);
      mentoring.setRoomExist(true);
      mentoringRepository.save(mentoring);
      groupChatRoomRepository.save(groupChatRoom);
      return Optional.of(GroupChatRoomCreateResponse.from(groupChatRoom));
    }
    return Optional.empty();
  }

  //  1:1 채팅방 생성
  @Transactional
  public PrivateChatRoom createPrivateRoom(
      PrivateChatRoomCreateRequest privateChatRoomCreateRequest, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND"));

    User mentor = userRepository.findById(privateChatRoomCreateRequest.getMentorId())
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "MENTOR_NOT_FOUND"));

    Mentoring mentoring = mentoringRepository.findById(
            privateChatRoomCreateRequest.getMentoringId())
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "MENTORING_NOT_FOUND"));

    if (!mentoring.getUser().getId().equals(mentor.getId())) {
      throw new AppException(HttpStatus.BAD_REQUEST, "Invalid mentor for the given mentoring");
    }

    if (mentor.getId().equals(userId)) {
      throw new AppException(HttpStatus.BAD_REQUEST, "사용자가 작성한 멘토링 글입니다");
    }

    if (privateChatRoomRepository.existsByUserIdAndMentoringId(user.getId(),
        privateChatRoomCreateRequest.getMentoringId())) {
      PrivateChatRoom existingChatRoom = privateChatRoomRepository.findByUserIdAndMentoringId(user.getId(),
              privateChatRoomCreateRequest.getMentoringId());
      Long privateChatRoomId = existingChatRoom.getId();
      throw new ChatRoomAlreadyExistsException(privateChatRoomId, "1:1 채팅창이 이미 생성되었습니다");
    }

    PrivateChatRoom privateChatRoom = new PrivateChatRoom(user, mentor, mentoring);
    PrivateChatRoom savePrivateChatRoom = privateChatRoomRepository.save(privateChatRoom);
    return savePrivateChatRoom;
  }

  // 그룹 채팅방 불러오기
  @Transactional
  public List<GroupMessage> findAllGroupMessages(Long mentoringId) {
    return groupMessageRepository.findByMentoringIdOrderByRegisterDatetime(mentoringId);
  }

  // 1:1 채팅방 불러오기
  @Transactional
  public List<PrivateMessage> findAllPrivateMessages(Long privateChatRoomId) {
    return privateMessageRepository.findByPrivateChatRoomIdOrderByRegisterDatetime(
        privateChatRoomId);
  }

  // 1:1 메세지 저장
  @Transactional
  public PrivateMessage savePrivateChatMessage(PrivateChatMessage privateChatMessage, String nickName) {
    log.debug("Enter savePrivateChatMessage method...");

    PrivateChatRoom privateChatRoom = privateChatRoomRepository.findById(
            privateChatMessage.getPrivateChatRoomId())
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "CHAT_ROOM_NOT_FOUND"));
    log.debug("PrivateChatRoom retrieved: {}", privateChatRoom);

    PrivateMessage privateMessage = new PrivateMessage(privateChatRoom,
        nickName, privateChatMessage.getMessage());
    privateMessageRepository.save(privateMessage);
    log.debug("Private message saved: {}", privateMessage);

    log.debug("Exit savePrivateChatMessage method...");
    return privateMessage;

  }

  // 그룹 메세지 저장
  @Transactional
  public GroupMessage saveGroupChatMessage(GroupChatMessage groupChatMessage, String nickName) {
    log.debug("Enter saveGroupChatMessage method...");
    Mentoring mentoring = mentoringRepository.findById(
            groupChatMessage.getGroupMentoringId())
        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Mentoring_NOT_FOUND"));
    log.debug("Mentoring retrieved: {}", mentoring);

    GroupMessage groupMessage = new GroupMessage(mentoring,
        nickName, groupChatMessage.getMessage());
    groupMessageRepository.save(groupMessage);
    log.debug("Group message saved: {}", groupMessage);

    log.debug("Exit saveGroupChatMessage method...");
    return groupMessage;
  }


  // 사용자의 1:1 채팅방 리스트 가져오기
  @Transactional
  public List<PrivateMessage> getPrivateMyChatListInfo(String email) {
    User user = userService.getUser(email);

    log.debug("Debug: user - {}", user);

    boolean isUser = privateChatRoomRepository.existsByUser(user);
    log.debug("Debug: isUser - {}", isUser);

    boolean isMentor = privateChatRoomRepository.existsByMentor(user);
    log.debug("Debug: isMentor - {}", isMentor);

    List<PrivateMessage> latestMessages = new ArrayList<>();

    if (isUser) {
      List<PrivateChatRoom> foundChatRoomsByUser = privateChatRoomRepository.findByUser(user);
      latestMessages.addAll(getLatestMessages(foundChatRoomsByUser));

      if (isMentor) {
        List<PrivateChatRoom> foundChatRoomsByMentor = privateChatRoomRepository.findByMentor(user);
        latestMessages.addAll(getLatestMessages(foundChatRoomsByMentor));
      }
    } else if (isMentor) {
      List<PrivateChatRoom> foundChatRoomsByMentor = privateChatRoomRepository.findByMentor(user);
      latestMessages.addAll(getLatestMessages(foundChatRoomsByMentor));

      if (isUser) {
        List<PrivateChatRoom> foundChatRoomsByUser = privateChatRoomRepository.findByUser(user);
        latestMessages.addAll(getLatestMessages(foundChatRoomsByUser));
      }
    }

    return latestMessages;
  }

  private List<PrivateMessage> getLatestMessages(List<PrivateChatRoom> chatRooms) {
    List<PrivateMessage> latestMessages = new ArrayList<>();

    for (PrivateChatRoom chatRoom : chatRooms) {
      // 각 채팅방의 메시지 리스트를 가져와서 정렬
      List<PrivateMessage> messages = chatRoom.getMessageList();
      messages.sort(Comparator.comparing(PrivateMessage::getRegisterDatetime).reversed());

      // 정렬된 메시지 리스트 중 첫 번째 메시지(최신 메시지)를 가져와서 결과 리스트에 추가
      if (!messages.isEmpty()) {
        latestMessages.add(messages.get(0));
      }
    }

    return latestMessages;
  }

}

