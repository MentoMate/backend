package com.example.mentoringproject.mentoring.event;

import com.example.mentoringproject.pay.service.PayService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayEventHandler {
  private final PayService payService;
  @EventListener
  public void payCancelByMentor(PayEvent event){
    payService.payCancelByMentor(event.getMentoring(), event.getRestApiKey(), event.getRestApiSecret());
  }

}
