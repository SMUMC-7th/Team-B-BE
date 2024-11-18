package com.example.teamB.domain.member.service.query;

import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.example.teamB.global.jwt.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    /** 본인 정보 조회 */
    @Override
    public MemberResponseDTO.MemberInfoDTO getProfile(Member member) {

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
    public Member getMember(String email) {
        return memberRepository.findByEmail(email).get();
    }
}
