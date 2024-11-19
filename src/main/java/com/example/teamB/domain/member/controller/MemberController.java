package com.example.teamB.domain.member.controller;

import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.service.command.MemberCommandService;
import com.example.teamB.domain.member.service.query.MemberQueryService;
import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.jwt.util.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final JwtProvider jwtProvider;

    /** 1단계: 회원가입 요청 DTO
     * 이메일, 비밀번호 입력
     * 이메일 중복 확인 및 인증 메일 전송*/
    @Operation(summary = "1. 회원가입 요청 및 인증", description = "회원가입을 위해 이메일로 인증 코드를 전송합니다.")
    @PostMapping("/signup/request-and-verify")
    public CustomResponse<Void> signupRequestAndVerifyEmail(@RequestBody @Valid MemberRequestDTO.SignupRequestAndVerifyEmailDTO dto) throws MessagingException {
        memberCommandService.signupRequestAndVerifyEmail(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 2단계: 인증 코드 검증 */
    @Operation(summary = "2. 회원가입 인증 코드 확인", description = "이메일로 전송된 인증 코드를 확인합니다.")
    @PostMapping("/signup/verify-code")
    public CustomResponse<Void> verifyCode(@RequestBody @Valid MemberRequestDTO.VerificationCodeDTO dto) {
        memberCommandService.verifyCode(dto.getEmail(), dto.getVerificationCode());
        return CustomResponse.onSuccess(null);
    }

    /** 3단계: 회원가입 완료 (+ 추가 정보 입력) */
    @Operation(summary = "3. 회원가입", description = "회원가입 시 추가 정보를 입력하고 계정을 생성합니다.")
    @PostMapping("/signup")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> signup(
            @RequestBody @Valid MemberRequestDTO.SignupDTO dto) {
        return CustomResponse.onSuccess(memberCommandService.signup(dto));
    }

    /** 회원 로그인 API */
    @Operation(summary = "로그인", description = "회원 로그인 후 토큰을 발급받습니다.")
    @PostMapping("/login")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> login(@RequestBody MemberRequestDTO.MemberLoginDTO dto) {
        return CustomResponse.onSuccess(memberCommandService.login(dto));
    }

    /** 회원 탈퇴 API */
    @Operation(summary = "회원 탈퇴", description = "회원 계정을 영구적으로 삭제합니다.")
    @PostMapping("/withdraw")
    public CustomResponse<Void> withdraw(@CurrentMember Member member) {
        memberCommandService.withdraw(member);
        return CustomResponse.onSuccess(null);
    }

    /** 비밀번호 변경 요청 */
    @Operation(summary = "1. 비밀번호 변경 요청", description = "사용자의 이메일로 비밀번호 변경 요청을 전송합니다.")
    @PostMapping("/password/change/request")
    public CustomResponse<Void> requestPasswordChange(@CurrentMember Member member) throws MessagingException {
        memberCommandService.requestPasswordChange(member);
        return CustomResponse.onSuccess(null);
    }

    /** 비밀번호 변경 이메일 인증 */
    @Operation(summary = "2. 비밀번호 변경 인증", description = "이메일로 전송된 인증 코드를 확인합니다.")
    @PostMapping("/password/change/verify")
    public CustomResponse<Void> verifyPasswordChangeCode(@RequestBody @Valid MemberRequestDTO.VerificationCodeDTO dto) {
        memberCommandService.verifyPasswordChangeCode(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 새 비밀번호 입력 */
    @Operation(summary = "3. 비밀번호 변경", description = "새로운 비밀번호를 설정합니다.")
    @PatchMapping("/password/change/complete")
    public CustomResponse<Void> completePasswordChange(@RequestBody @Valid MemberRequestDTO.PasswordChangeCompleteDTO dto) {
        memberCommandService.completePasswordChange(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 닉네임 변경 */
    @Operation(summary = "닉네임 변경", description = "회원의 닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public CustomResponse<Void> changeNickname(
            @CurrentMember Member member,
            @RequestBody @Valid MemberRequestDTO.ChangeNicknameDTO dto) {
        memberCommandService.changeNickname(member, dto.getNewNickname());
        return CustomResponse.onSuccess(null);
    }

    /** 알람 설정 변경 */
    @Operation(summary = "알람 설정 변경", description = "회원의 알람 상태와 시간을 변경합니다.")
    @PatchMapping("/alarm-settings")
    public CustomResponse<Void> changeAlarmSettings(
            @CurrentMember Member member,
            @RequestBody @Valid MemberRequestDTO.ChangeAlarmSettingsDTO dto) {
        memberCommandService.changeAlarmSettings(member, dto.getAlarmStatus(), dto.getAlarmTime());
        return CustomResponse.onSuccess(null);
    }

    /** 자기 정보 조회 */
    @Operation(summary = "내 정보 조회", description = "로그인된 회원의 프로필 정보를 조회합니다.")
    @GetMapping("/profile")
    public CustomResponse<MemberResponseDTO.MemberInfoDTO> getMyInfo(@CurrentMember Member member) {
        MemberResponseDTO.MemberInfoDTO myInfo = memberQueryService.getProfile(member);
        return CustomResponse.onSuccess(myInfo);
    }

}