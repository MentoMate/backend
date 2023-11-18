package com.example.mentoringproject.mentoring.event;

import com.example.mentoringproject.mentoring.mentoring.entity.Mentoring;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PayEvent extends ApplicationEvent {
  private Mentoring mentoring;
  private String restApiKey;
  private String restApiSecret;

  public PayEvent(Object source, Mentoring mentoring, String restApiKey, String restApiSecret){
    super(source);
    this.mentoring = mentoring;
    this.restApiKey = restApiKey;
    this.restApiSecret = restApiSecret;
  }

}
