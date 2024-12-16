package com.example.teamB.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoAuthResponseDTO {
    private boolean success;
    private String accessToken; // JWT Access Token
    private String refreshToken; // JWT Refresh Token
    private String message; // 성공 메시지
}
