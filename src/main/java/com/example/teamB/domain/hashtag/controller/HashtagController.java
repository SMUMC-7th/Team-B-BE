package com.example.teamB.domain.hashtag.controller;

import com.example.teamB.domain.hashtag.dto.HashtagResponseDTO;
import com.example.teamB.domain.hashtag.service.query.HashtagQueryService;
import com.example.teamB.domain.member.annotation.CurrentMember;
import com.example.teamB.domain.member.entity.Member;
import com.example.teamB.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Hashtag API")
public class HashtagController {

    private final HashtagQueryService hashtagQueryService;

    @GetMapping("/recommendation")
    @Operation(summary = "오늘의 옷(해시태그) 추천 조회", description = "해시태그와 각 해시태그에 맞는 옷 사진을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OOTD002", description = "적절한 날씨 분류를 찾지 못했습니다. 기온 입력정보를 다시 확인해주세요", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
    })
    @Parameters({
            @Parameter(name = "maxTemperature", description = "날씨 Api로 조회한 당일 최고 기온"),
            @Parameter(name = "minTemperature", description = "날씨 Api로 조회한 당일 최저 기온")
    })
    public CustomResponse<HashtagResponseDTO.DailyRecommendationListDTO> getRecommendation (
            @CurrentMember Member member,
            @RequestParam(value = "maxTemperature") int maxTemperature,
            @RequestParam(value = "minTemperature") int minTemperature) {
        return CustomResponse.onSuccess(hashtagQueryService.getRecommendation(maxTemperature, minTemperature));
    }
}
