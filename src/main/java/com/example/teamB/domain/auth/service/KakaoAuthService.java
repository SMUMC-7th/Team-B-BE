package com.example.teamB.domain.auth.service;

import com.example.teamB.domain.auth.dto.KakaoAuthResponseDTO;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public KakaoAuthResponseDTO kakaoLogin(String accessToken) {
        // 1. 카카오 API 호출
        ResponseEntity<JsonNode> response = callKakaoApi(accessToken);

        // 2. 사용자 정보 추출
        JsonNode userInfo = response.getBody();
        String kakaoId = userInfo.get("id").asText();
        String nickname = userInfo.path("kakao_account").path("profile").path("nickname").asText();
        String profileImageUrl = userInfo.path("kakao_account").path("profile").path("thumbnail_image_url").asText();

        // 3. DB에 사용자 정보 저장 또는 조회
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .kakaoId(kakaoId)
                        .nickname(nickname)
                        .profileImageUrl(profileImageUrl)
                        .build()));

        // 4. 성공 응답 반환
        return KakaoAuthResponseDTO.builder()
                .success(true)
                .message("카카오 인증 및 사용자 정보 저장 완료")
                .build();
    }

    private ResponseEntity<JsonNode> callKakaoApi(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        return restTemplate.exchange(KAKAO_USER_INFO_URI, HttpMethod.GET, request, JsonNode.class);
    }
}
