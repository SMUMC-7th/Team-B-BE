package com.example.teamB.domain.member.dto;

import com.example.teamB.domain.member.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 클라이언트에서 서버로 요청할 때 사용
public class MemberRequestDTO {

    /** 1단계: 회원가입 요청 DTO
     * 이메일, 비밀번호 입력
     * 이메일 중복 확인 및 인증 메일 전송*/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupRequestAndVerifyEmailDTO {

        private String email;

        private String password;
    }

    /** 2단계: 인증 코드 검증 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VerificationCodeDTO {

        private String email;

        private String verificationCode;
    }

    /** 3단계: 회원가입 완료 (+ 추가 정보 입력) */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupDTO {

        private String email;
        private String name;
        private String nickname;
        private Gender gender;
    }


    /** 로그인 요청 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberLoginDTO {

        private String email;

        private String password;
    }

    /** 비밀번호 변경 요청 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordChangeRequestDTO {
        @Email
        private String email;
    }

    /** 비밀번호 변경 완료 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordChangeCompleteDTO {
        @Email
        private String email;

        @NotBlank
        private String newPassword;
    }

    /** 닉네임 변경 요청 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeNicknameDTO {
        @NotBlank
        private String newNickname;
    }

    /** 알람 설정 변경 요청 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeAlarmSettingsDTO {
        private Boolean alarmStatus; // 알람 활성화 여부
        private String alarmTime;    // 알람 시간, "HH:mm" 형식
    }
}