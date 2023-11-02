package com.example.mentoringproject.notification.notification.emitter.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

  SseEmitter save(String emitterId, SseEmitter sseEmitter);

  Map<String, SseEmitter> findAllEmitterStartWithByUserEmail(String userEmail);

  void deleteById(String id);
}
