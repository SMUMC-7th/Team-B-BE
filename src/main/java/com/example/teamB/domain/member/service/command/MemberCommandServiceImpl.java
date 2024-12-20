package com.example.teamB.domain.member.service.command;

import com.example.teamB.domain.member.enums.MemberStatus;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.service.query.MemberQueryService;
import com.example.teamB.global.jwt.util.JwtProvider;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final EmailCommandService emailCommandService;
    private final MemberQueryService memberQueryService;


    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, String> passwordsMap = new ConcurrentHashMap<>();

    /** 1단계: 회원가입 요청 및 이메일 인증 */
    @Override
    public void signupRequestAndVerifyEmail(MemberRequestDTO.SignupRequestAndVerifyEmailDTO dto) throws MessagingException {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new MemberException(MemberErrorCode.ALREADY_EXIST);
        }

        // 비밀번호 암호화 및 저장
        String encodedPassword = encoder.encode(dto.getPassword());
        passwordsMap.put(dto.getEmail(), encodedPassword);

        // 인증 코드 생성 및 이메일 전송
        String verificationCode = emailCommandService.sendVerificationEmail(dto.getEmail());
        verificationCodes.put(dto.getEmail(), verificationCode);

        log.info("Signup request and email verification initiated for: {}", dto.getEmail());
    }

    /** 2단계: 인증 코드 검증 */
    @Override
    public void verifyCode(String email, String verificationCode) {
        String storedCode = verificationCodes.get(email);
        if (storedCode == null || !storedCode.equals(verificationCode)) {
            throw new MemberException(MemberErrorCode.INVALID_VERIFICATION_CODE);
        }
        log.info("Verification code successfully validated for email: {}", email);
    }

    /** 3단계: 회원가입 완료 (+ 추가 정보 입력) */
    @Override
    public MemberResponseDTO.MemberTokenDTO signup(MemberRequestDTO.SignupDTO dto) {
        log.info("Completing signup for email: {}", dto.getEmail());

        String emailKey = dto.getEmail().trim().toLowerCase();

        // 인증 코드 검증
        if (!verificationCodes.containsKey(emailKey)) {
            log.error("Email not verified: {}", emailKey);
            throw new MemberException(MemberErrorCode.UNVERIFIED_EMAIL);
        }

        // 비밀번호 확인
        String encodedPassword = passwordsMap.get(emailKey);
        if (encodedPassword == null) {
            log.error("No password found for email: {}", emailKey);
            throw new MemberException(MemberErrorCode.UNVERIFIED_EMAIL);
        }

        // Member 저장
        Member member = Member.builder()
                .email(emailKey)
                .password(encodedPassword)
                .name(dto.getName())
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .alarmStatus(false) // 기본 알람 상태
                .alarmTime(LocalTime.of(9, 0)) // 기본 알람 시간: 오전 9시
                .build();

        try {
            member = memberRepository.save(member);
            log.info("Member saved successfully: {}", member);
        } catch (Exception e) {
            log.error("Error while saving member to the database", e);
            throw e;
        }

        // 데이터 정리
        verificationCodes.remove(emailKey);
        passwordsMap.remove(emailKey);

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(member))
                .refreshToken(jwtProvider.createRefreshToken(member))
                .build();
    }


    /** 로그인 */
    @Override
    public MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.MemberLoginDTO dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 탈퇴 상태 확인
        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new MemberException(MemberErrorCode.INACTIVE_ACCOUNT);
        }

        if (!encoder.matches(dto.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
        }

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(member))
                .refreshToken(jwtProvider.createRefreshToken(member))
                .build();
    }


    /** 회원 탈퇴 */
    @Override
    public void withdraw(Member member) {
        memberRepository.delete(member);
        log.info("Member with email {} has been deleted.", member.getEmail());
    }

    /** 비밀번호 변경 요청 */
    @Override
    public void requestPasswordChange(Member member) throws MessagingException {

        // 인증 코드 생성 및 이메일 전송
        String verificationCode = emailCommandService.sendVerificationEmail(member.getEmail());
        verificationCodes.put(member.getEmail(), verificationCode);

        log.info("Password change request initiated for {}", member.getEmail());
    }

    /** 토큰 재발급 */
    @Override
    public MemberResponseDTO.MemberTokenDTO refreshToken(String refreshToken) {
        // Refresh Token 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        // Refresh Token에서 이메일 추출
        String tokenEmail = jwtProvider.getEmail(refreshToken);

        // 해당 이메일의 회원 정보 가져오기
        Member member = memberRepository.findByEmail(tokenEmail)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        // 새로운 토큰 생성
        String newAccessToken = jwtProvider.createAccessToken(member);
        String newRefreshToken = jwtProvider.createRefreshToken(member);

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /** 인증 코드 확인 */
    @Override
    public void verifyPasswordChangeCode(Member member, String verificationCode) {
        String storedCode = verificationCodes.get(member.getEmail());
        if (storedCode == null || !storedCode.equals(verificationCode)) {
            throw new MemberException(MemberErrorCode.INVALID_VERIFICATION_CODE);
        }
        log.info("Password change verification successful for {}", member.getEmail());
    }

    /** 새 비밀번호 설정*/
    @Override
    public void completePasswordChange(Member member, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        member.setPassword(encodedPassword);
        memberRepository.save(member);

        verificationCodes.remove(member.getEmail());
        log.info("Password successfully changed for {}", member.getEmail());
    }

    /** 닉네임 변경 */
    @Override
    @Transactional
    public void changeNickname(Member member, String newNickname) {
        member.setNickname(newNickname);
        memberRepository.save(member);
        log.info("Nickname changed successfully for member: {}", member.getEmail());
    }

    /** 알람 세팅 변경 */
    @Override
    public void changeAlarmSettings(Member member, Boolean alarmStatus, String alarmTime) {
        member.setAlarmStatus(alarmStatus);
        member.setAlarmTime(LocalTime.parse(alarmTime));
        memberRepository.save(member);
        log.info("Alarm settings updated for member: {}", member.getEmail());
    }

}
