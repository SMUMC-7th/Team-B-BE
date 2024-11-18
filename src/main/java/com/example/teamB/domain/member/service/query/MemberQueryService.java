package com.example.teamB.domain.member.service.query;

import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;

public interface MemberQueryService {
    MemberResponseDTO.MemberInfoDTO getProfile(Member member); // 본인 정보 조회
    Object getMember(String email);
}
