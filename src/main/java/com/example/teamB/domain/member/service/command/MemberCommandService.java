package com.example.teamB.domain.member.service.command;
import com.example.teamB.domain.member.entity.Member;
import jakarta.mail.MessagingException;

import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;

public interface MemberCommandService {
    void signupRequestAndVerifyEmail(MemberRequestDTO.SignupRequestAndVerifyEmailDTO dto) throws MessagingException; // 회원가입 요청 및 인증메일 전송
    void verifyCode(String email, String verificationCode); // 인증 코드 검증
    void addAdditionalInfo(MemberRequestDTO.AdditionalInfoDTO dto); // 추가 정보 입력
    MemberResponseDTO.MemberTokenDTO completeSignup(MemberRequestDTO.SignupCompleteDTO dto); // 회원가입 완료
    MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.MemberLoginDTO dto); // 로그인
    Member getMemberFromToken(String accessToken);
    void withdraw(String accessToken); // 탈퇴
    void requestPasswordChange(MemberRequestDTO.PasswordChangeRequestDTO dto) throws MessagingException; // 비밀번호 변경 요청
    void verifyPasswordChangeCode(MemberRequestDTO.VerificationCodeDTO dto); // 비밀번호 변경 인증 코드 검증
    void completePasswordChange(MemberRequestDTO.PasswordChangeCompleteDTO dto); // 비밀번호 변경 완료
    void changeNickname(String accessToken, String newNickname); // 닉네임 변경
    void changeAlarmSettings(String accessToken, Boolean alarmStatus, String alarmTime); // 알람 설정 변경\
    MemberResponseDTO.MemberInfoDTO getProfile(String accessToken); // 본인 정보 조회
}
