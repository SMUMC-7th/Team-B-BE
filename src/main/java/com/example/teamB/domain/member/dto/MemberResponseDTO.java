package com.example.teamB.domain.member.dto;

import com.example.teamB.domain.member.enums.Gender;
import com.example.teamB.domain.member.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

// 서버에서 클라이언트로 응답할 때 사용
public class MemberResponseDTO {

    // 토큰 정보 응답
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberTokenDTO {
        String accessToken;
        String refreshToken;
    }

    // 회원 정보 응답 시 필요한 정보
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfoDTO {
        private String email;
        private String name;
        private String nickname;
        private Gender gender;
        private Boolean alarmStatus;
        private LocalTime alarmTime;
    }


}
