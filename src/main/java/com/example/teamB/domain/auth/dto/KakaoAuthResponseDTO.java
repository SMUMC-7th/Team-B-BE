package com.example.teamB.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoAuthResponseDTO {
    private boolean success;
    private String message;
}
