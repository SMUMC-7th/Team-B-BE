package com.example.teamB.domain.auth.controller;

import com.example.teamB.domain.auth.dto.KakaoAuthRequestDTO;
import com.example.teamB.domain.auth.dto.KakaoAuthResponseDTO;
import com.example.teamB.domain.auth.service.KakaoAuthService;
import com.example.teamB.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인 처리", description = "카카오 Access Token을 검증하고 회원가입/로그인을 수행합니다.")
    @ApiResponse(responseCode = "200", description = "카카오 인증 및 사용자 정보 저장 완료",
            content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    @PostMapping("/kakao")
    public CustomResponse<KakaoAuthResponseDTO> kakaoLogin(@RequestBody KakaoAuthRequestDTO request) {
        KakaoAuthResponseDTO response = kakaoAuthService.kakaoLogin(request.getAccessToken());
        return CustomResponse.onSuccess(response);
    }
}
