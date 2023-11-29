package com.example.mentoringproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@CrossOrigin(origins = {"https://www.mentormate.xyz"})
public class MentoringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentoringProjectApplication.class, args);
    }

}
