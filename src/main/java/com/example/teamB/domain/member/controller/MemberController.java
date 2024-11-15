package com.example.teamB.domain.member.controller;

import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.member.service.command.MemberCommandService;
import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.apiPayload.code.BaseSuccessCode;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;

    /** 1단계: 회원가입 요청 (이메일, 비밀번호 입력) */
    @PostMapping("/signup/request")
    public CustomResponse<Void> signupRequest(@RequestBody MemberRequestDTO.SignupRequestDTO dto) throws MessagingException {
        memberCommandService.signupRequest(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 2단계: 이메일 중복 확인 및 인증 메일 전송 */
    @PostMapping("/signup/verify-email")
    public CustomResponse<Void> sendVerificationEmail(@RequestBody MemberRequestDTO.EmailVerificationRequestDTO dto) throws MessagingException {
        memberCommandService.sendVerificationEmail(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 3단계: 인증 코드 검증 */
    @PostMapping("/signup/verify-code")
    public CustomResponse<Void> verifyCode(@RequestBody @Valid MemberRequestDTO.VerificationCodeDTO dto) {
        memberCommandService.verifyCode(dto.getEmail(), dto.getVerificationCode());
        return CustomResponse.onSuccess(null);
    }

    /** 4단계: 추가 정보 입력 (이름, 닉네임, 젠더 입력) */
    @PostMapping("/signup/additional-info")
    public CustomResponse<Void> addAdditionalInfo(@RequestBody @Valid MemberRequestDTO.AdditionalInfoDTO dto) {
        memberCommandService.addAdditionalInfo(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 5단계: 회원가입 완료 */
    @PostMapping("/signup/complete")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> completeSignup(@RequestBody @Valid MemberRequestDTO.SignupCompleteDTO dto) {
        return CustomResponse.onSuccess(memberCommandService.completeSignup(dto));
    }

    /** 회원 로그인 API */
    @PostMapping("/login")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> login(@RequestBody MemberRequestDTO.MemberLoginDTO dto) {
        return CustomResponse.onSuccess(memberCommandService.login(dto));
    }
}