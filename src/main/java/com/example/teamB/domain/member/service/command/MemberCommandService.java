package com.example.teamB.domain.member.service.command;
import jakarta.mail.MessagingException;

import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;

public interface MemberCommandService {
    void signupRequest(MemberRequestDTO.SignupRequestDTO dto) throws MessagingException; // 회원가입 요청
    void sendVerificationEmail(MemberRequestDTO.EmailVerificationRequestDTO dto) throws MessagingException; // 이메일 인증 메일 전송
    void verifyCode(String email, String verificationCode); // 인증 코드 검증
    void addAdditionalInfo(MemberRequestDTO.AdditionalInfoDTO dto); // 추가 정보 입력
    MemberResponseDTO.MemberTokenDTO completeSignup(MemberRequestDTO.SignupCompleteDTO dto); // 회원가입 완료
    MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.MemberLoginDTO dto); // 로그인
}
