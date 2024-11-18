package com.example.teamB.domain.member.service.query;

import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.exception.MemberErrorCode;
import com.example.teamB.domain.member.exception.MemberException;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.domain.member.service.command.EmailCommandService;
import com.example.teamB.global.jwt.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    /** JWT를 이용한 회원 조회 */
    @Override
    public Member getMemberFromToken(String accessToken) {
        // JWT 유효성 검사
        if (!jwtProvider.validateToken(accessToken)) {
            throw new MemberException(MemberErrorCode.INVALID_TOKEN);
        }

        // JWT에서 이메일 추출
        String email = jwtProvider.getEmail(accessToken);

        // 이메일로 사용자 조회
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }

    /** 본인 정보 조회 */
    @Override
    public MemberResponseDTO.MemberInfoDTO getProfile(String accessToken) {
        Member member = getMemberFromToken(accessToken);

        return MemberResponseDTO.MemberInfoDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .alarmStatus(member.getAlarmStatus())
                .alarmTime(member.getAlarmTime())
                .build();
    }

    @Override
    public Object getMember(String email) {
        return null;
    }
}
