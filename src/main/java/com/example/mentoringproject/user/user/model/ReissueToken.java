package com.example.mentoringproject.user.user.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReissueToken {
    private String accessToken;
    private String refreshToken;
}
