package com.example.teamB.domain.member.service.query;

import com.example.teamB.domain.member.dto.MemberResponseDTO;
import com.example.teamB.domain.member.entity.Member;

public interface MemberQueryService {
    Member getMemberFromToken(String accessToken);
    MemberResponseDTO.MemberInfoDTO getProfile(String accessToken); // 본인 정보 조회

    Object getMember(String email);
}
