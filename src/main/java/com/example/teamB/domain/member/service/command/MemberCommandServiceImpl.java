package com.example.teamB.domain.member.service.command;

import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.member.dto.MemberRequestDTO;
import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.global.jwt.util.JwtProvider;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, MemberRequestDTO.AdditionalInfoDTO> additionalInfoMap = new ConcurrentHashMap<>();
    private final Map<String, String> passwordsMap = new ConcurrentHashMap<>();

    /** 1단계: 회원가입 요청 */
    @Override
    public void signupRequest(MemberRequestDTO.SignupRequestDTO dto) {
        log.info("Signup request for email: {}", dto.getEmail());

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new MemberException(MemberErrorCode.ALREADY_EXIST);
        }

        String encodedPassword = encoder.encode(dto.getPassword());
        log.info("Encoded password: {}", encodedPassword);

        passwordsMap.put(dto.getEmail(), encodedPassword);
        log.info("Password stored in passwordsMap for email: {}", dto.getEmail());
    }

    /** 2단계: 이메일 인증 코드 전송 */
    @Override
    public void sendVerificationEmail(MemberRequestDTO.EmailVerificationRequestDTO dto) throws MessagingException {
        String verificationCode = emailCommandService.sendVerificationEmail(dto.getEmail());
        verificationCodes.put(dto.getEmail(), verificationCode);
    }

    /** 3단계: 인증 코드 검증 */
    @Override
    public void verifyCode(String email, String verificationCode) {
        String storedCode = verificationCodes.get(email);
        if (storedCode == null || !storedCode.equals(verificationCode)) {
            throw new MemberException(MemberErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    /** 4단계: 추가 정보 입력 */
    @Override
    public void addAdditionalInfo(MemberRequestDTO.AdditionalInfoDTO dto) {
        log.info("Storing additional info: {}", dto);
        additionalInfoMap.put(dto.getEmail(), MemberRequestDTO.AdditionalInfoDTO.builder()
                .name(dto.getName())
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .build());
        log.info("Current additionalInfoMap: {}", additionalInfoMap);
    }

    /** 5단계: 회원가입 완료 */
    @Override
    public MemberResponseDTO.MemberTokenDTO completeSignup(MemberRequestDTO.SignupCompleteDTO dto) {
        log.info("Completing signup for email: {}", dto.getEmail());

        String emailKey = dto.getEmail().trim().toLowerCase();

        if (!verificationCodes.containsKey(emailKey)) {
            log.error("Email not verified: {}", emailKey);
            throw new MemberException(MemberErrorCode.UNVERIFIED_EMAIL);
        }

        MemberRequestDTO.AdditionalInfoDTO additionalInfo = additionalInfoMap.get(emailKey);
        if (additionalInfo == null) {
            log.error("No additional info found for email: {}", emailKey);
            throw new MemberException(MemberErrorCode.INVALID_VERIFICATION_CODE);
        }
        log.info("Retrieved additional info: {}", additionalInfo);

        String encodedPassword = passwordsMap.get(emailKey);
        if (encodedPassword == null) {
            log.error("No password found for email: {}", emailKey);
            throw new MemberException(MemberErrorCode.UNVERIFIED_EMAIL);
        }
        log.info("Retrieved encoded password for email: {}", emailKey);

        // Member 저장
        Member member = Member.builder()
                .email(emailKey)
                .password(encodedPassword)
                .name(additionalInfo.getName())
                .nickname(additionalInfo.getNickname())
                .gender(additionalInfo.getGender())
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
        additionalInfoMap.remove(emailKey);
        passwordsMap.remove(emailKey);

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(member))
                .refreshToken(jwtProvider.createRefreshToken(member))
                .build();
    }

    /** 로그인 처리 */
    @Override
    public MemberResponseDTO.MemberTokenDTO login(MemberRequestDTO.MemberLoginDTO dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        if (!encoder.matches(dto.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
        }

        return MemberResponseDTO.MemberTokenDTO.builder()
                .accessToken(jwtProvider.createAccessToken(member))
                .refreshToken(jwtProvider.createRefreshToken(member))
                .build();
    }
}