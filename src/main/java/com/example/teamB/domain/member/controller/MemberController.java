package com.example.teamB.domain.member.controller;

import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.service.command.MemberCommandService;
import com.example.teamB.domain.member.service.query.MemberQueryService;
import com.example.teamB.global.apiPayload.CustomResponse;
import com.example.teamB.global.jwt.util.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Parameters({
            @Parameter(name = "dto", description = "회원가입 요청을 위한 이메일 및 비밀번호 입력")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 메일 전송을 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "500", description = "메일 전송 실패하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/signup/request-and-verify")
    public CustomResponse<Void> signupRequestAndVerifyEmail(@RequestBody @Valid MemberRequestDTO.SignupRequestAndVerifyEmailDTO dto) throws MessagingException {
        memberCommandService.signupRequestAndVerifyEmail(dto);
        return CustomResponse.onSuccess(null);
    }

    /** 2단계: 인증 코드 검증 */
    @Operation(summary = "2. 회원가입 인증 코드 확인", description = "이메일로 전송된 인증 코드를 확인합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "인증 코드 확인을 위한 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 코드 확인에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 인증코드입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "인증 코드가 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/signup/verify-code")
    public CustomResponse<Void> verifyCode(@RequestBody @Valid MemberRequestDTO.VerificationCodeDTO dto) {
        memberCommandService.verifyCode(dto.getEmail(), dto.getVerificationCode());
        return CustomResponse.onSuccess(null);
    }

    /** 3단계: 회원가입 완료 (+ 추가 정보 입력) */
    @Operation(summary = "3. 회원가입", description = "회원가입 시 추가 정보를 입력하고 계정을 생성합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "회원가입 완료를 위한 추가 정보 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "인증되지 않은 이메일입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/signup")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> signup(
            @RequestBody @Valid MemberRequestDTO.SignupDTO dto) {
        return CustomResponse.onSuccess(memberCommandService.signup(dto));
    }

    /** 회원 로그인 API */
    @Operation(summary = "로그인", description = "회원 로그인 후 토큰을 발급받습니다.")
    @Parameters({
            @Parameter(name = "dto", description = "로그인을 위한 이메일과 비밀번호 입력 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호가 틀립니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "403", description = "비활성화된 계정입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/login")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> login(@RequestBody MemberRequestDTO.MemberLoginDTO dto) {
        return CustomResponse.onSuccess(memberCommandService.login(dto));
    }

    /** 토큰 재발급 API */
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.")
    @Parameters({
            @Parameter(name = "Refresh-Token", description = "리프레시 토큰")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰이 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/token/reissue")
    public CustomResponse<MemberResponseDTO.MemberTokenDTO> refreshToken(
            @RequestHeader(value = "Refresh-Token", required = true) String refreshToken) {

        // Refresh Token이 비어 있거나 잘못된 경우 처리
        if (refreshToken == null || refreshToken.isBlank()) {
            log.error("Refresh token is missing or invalid.");
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        // 토큰 재발급
        MemberResponseDTO.MemberTokenDTO tokenResponse = memberCommandService.refreshToken(refreshToken);
        return CustomResponse.onSuccess(tokenResponse);
    }

    /** 회원 탈퇴 API */
    @Operation(summary = "회원 탈퇴", description = "회원 계정을 영구적으로 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/withdraw")
    public CustomResponse<Void> withdraw(@CurrentMember Member member) {
        memberCommandService.withdraw(member);
        return CustomResponse.onSuccess(null);
    }

    /** 비밀번호 변경 요청 */
    @Operation(summary = "1. 비밀번호 변경 요청", description = "사용자의 이메일로 비밀번호 변경 요청을 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 요청 메일 전송 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "500", description = "메일 전송 실패하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/password/change/request")
    public CustomResponse<Void> requestPasswordChange(@CurrentMember Member member) throws MessagingException {
        memberCommandService.requestPasswordChange(member);
        return CustomResponse.onSuccess(null);
    }

    /** 비밀번호 변경 이메일 인증 */
    @Operation(summary = "2. 비밀번호 변경 인증", description = "이메일로 전송된 인증 코드를 확인합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "인증 코드 입력 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 코드 확인에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 인증 코드입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "인증 코드 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PostMapping("/password/change/verify")
    public CustomResponse<Void> verifyPasswordChangeCode(
            @CurrentMember Member member,
            @RequestBody @Valid MemberRequestDTO.PasswordChangeVerificationDTO dto) {
        memberCommandService.verifyPasswordChangeCode(member, dto.getVerificationCode());
        return CustomResponse.onSuccess(null);
    }

    /** 새 비밀번호 입력 */
    @Operation(summary = "3. 비밀번호 변경", description = "새로운 비밀번호를 설정합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "새 비밀번호 입력 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 비밀번호 설정 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 유효성 검증 실패하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PatchMapping("/password/change/complete")
    public CustomResponse<Void> completePasswordChange(
            @CurrentMember Member member,
            @RequestBody @Valid MemberRequestDTO.PasswordChangeCompleteDTO dto) {
        memberCommandService.completePasswordChange(member, dto.getNewPassword());
        return CustomResponse.onSuccess(null);
    }

    /** 닉네임 변경 */
    @Operation(summary = "닉네임 변경", description = "회원의 닉네임을 변경합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "새 닉네임 변경을 위한 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임 변경에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "닉네임 유효성 검증 실패하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PatchMapping("/nickname")
    public CustomResponse<Void> changeNickname(
            @CurrentMember Member member,
            @RequestBody @Valid MemberRequestDTO.ChangeNicknameDTO dto) {
        memberCommandService.changeNickname(member, dto.getNewNickname());
        return CustomResponse.onSuccess(null);
    }

    /** 알람 설정 변경 */
    @Operation(summary = "알람 설정 변경", description = "회원의 알람 상태와 시간을 변경합니다.")
    @Parameters({
            @Parameter(name = "dto", description = "알람 상태 및 시간을 설정하는 DTO")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알람 설정 변경 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "알람 시간 형식 오류입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @PatchMapping("/alarm-settings")
    public CustomResponse<Void> changeAlarmSettings(
            @CurrentMember Member member,
            @RequestBody @Valid MemberRequestDTO.ChangeAlarmSettingsDTO dto) {
        memberCommandService.changeAlarmSettings(member, dto.getAlarmStatus(), dto.getAlarmTime());
        return CustomResponse.onSuccess(null);
    }

    /** 자기 정보 조회 */
    @Operation(summary = "내 정보 조회", description = "로그인된 회원의 프로필 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 정보 조회에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @GetMapping("/profile")
    public CustomResponse<MemberResponseDTO.MemberInfoDTO> getMyInfo(@CurrentMember Member member) {
        MemberResponseDTO.MemberInfoDTO myInfo = memberQueryService.getProfile(member);
        return CustomResponse.onSuccess(myInfo);
    }

}