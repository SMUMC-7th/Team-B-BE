package com.example.teamB.domain.auth.controller;

import com.example.teamB.domain.auth.dto.KakaoAuthRequestDTO;
import com.example.teamB.domain.auth.dto.KakaoAuthResponseDTO;
import com.example.teamB.domain.auth.service.KakaoAuthService;
import com.example.teamB.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인/회원가입", description = "카카오 Access Token을 받아서 로그인 또는 회원가입을 처리합니다.")
    @Parameter(name = "accessToken", description = "카카오에서 받은 Access Token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카카오 로그인/회원가입 성공",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "401", description = "카카오 Access Token이 유효하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    public CustomResponse<KakaoAuthResponseDTO> kakaoAuth(@RequestBody @Valid KakaoAuthRequestDTO requestDTO) {
        KakaoAuthResponseDTO response = kakaoAuthService.kakaoLogin(requestDTO.getAccessToken());
        return CustomResponse.onSuccess(response);
    }
}
